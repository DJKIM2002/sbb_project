package com.mysite.sbb.question;

import java.util.List;

import com.mysite.sbb.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	Question findBySubject(String subject);
	// subject 속성의 값을 기준으로 데이터 조회
	Question findBySubjectAndContent(String subject, String content);
	// subject 속성과 content 속성의 값을 기준으로 데이터 조회
	List<Question> findBySubjectLike(String subject);
	// subject 열 값들 중에 특정 문자열을 포함하는 데이터를 조회
	Page<Question> findAll(Pageable pageable);
	// 모든 데이터를 조회(페이지네이션 적용)
//	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	// Specification을 이용한 검색 기능 구현
	
	@Query("select "
            + "distinct q "
            + "from Question q " 
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
	// JPQL을 이용한 검색 기능 구현

	@Query("SELECT q FROM Question q WHERE q.category = :category AND (q.subject LIKE %:kw% OR q.content LIKE %:kw%)")
	Page<Question> findByCategoryAndKeyword(@Param("category") Category category, @Param("kw") String kw, Pageable pageable);

}
