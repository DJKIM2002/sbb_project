package com.mysite.sbb.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	// 특정 질문의 답변을 최신순 정렬 (createDate 기준 내림차순)
	@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId ORDER BY a.createDate DESC")
	Page<Answer> findByQuestionIdOrderByCreateDateDesc(Integer questionId, Pageable pageable);

	// 특정 질문의 답변을 추천순 정렬 (추천 개수 기준 내림차순, 동률이면 최신순 정렬)
	@Query(value = "SELECT a.*, COUNT(v.voter_id) AS voterCount FROM answer a LEFT JOIN answer_voter v ON a.id = v.answer_id WHERE a.question_id = :questionId GROUP BY a.id ORDER BY voterCount DESC, a.create_date DESC", nativeQuery = true)
	Page<Answer> findByQuestionIdOrderByVoterCountDesc(Integer questionId, Pageable pageable);


}
