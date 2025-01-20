package com.mysite.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

@SpringBootTest
// 스프링부트의 테스트 클래스임을 알리는 어노테이션
class SbbApplicationTests {

	@Autowired
	// 의존성 주입 어노테이션
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private QuestionService questionService;
	
//	@Transactional
	// 메서드가 종료될 때까지 DB 세션이 유지되게 하는 어노테이션
	@Test
	// 테스트 메서드임을 알리는 어노테이션
	void testJpa() {
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대해서 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q1);
//		
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q2);
		
//		List<Question> all = this.questionRepository.findAll();
		// question 테이블에 저장된 모든 데이터를 조회(SELECT * FROM question;)
//        assertEquals(2, all.size());
        // assertEquals(기댓값, 실젯값) : 테스트에서 예상한 결과와 실제 결과가 동일한지 확인
        // DB에 저장된 데이터가 현재 2행이므로, 모든 데이터를 조회했을 때, 결과로 나타난 데이터 사이즈는 2가 되어야 한다.
        // 만약 설정한 기댓값과 실젯값이 같지 않다면, 테스트는 실패 처리된다.

//        Question q = all.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
        // "sbb가 무엇인가요?"라는 질문이 데이터베이스에 저장되어 있지 않거나, 가져오지 못했을 경우 실패 처리 된다.
		
//		Optional<Question> oq = this.questionRepository.findById(1);
//        if(oq.isPresent()) {
//            Question q = oq.get();
//            assertEquals("sbb가 무엇인가요?", q.getSubject());
//        }
        
        // find() 메서드의 경우, get() 메서드와 달리 호출한 값이 존재할 수도, 존재하지 않을 수도 있어서 리턴 타입으로 Optional을 사용
        // Optional : null 값을 유연하게 처리하기 위한 클래스, isPresent() 메서드로 값이 존재하는지 확인할 수 있음.
		
//		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//		assertEquals(1, q.getId());
		
//		Question q = this.questionRepository.findBySubjectAndContent(
//                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//        assertEquals(1, q.getId());
        
//        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
//        Question q = qList.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
		
//		Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        q.setSubject("수정된 제목");
//        this.questionRepository.save(q);
		
//		assertEquals(2, this.questionRepository.count());
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        this.questionRepository.delete(q);
//        assertEquals(1, this.questionRepository.count());
		
//		Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        Answer a = new Answer();
//        a.setContent("네 자동으로 생성됩니다.");
//        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요
//        a.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a);
//        
//        Optional<Answer> oa = this.answerRepository.findById(1);
//        assertTrue(oa.isPresent());
//        Answer a = oa.get();
//        assertEquals(2, a.getQuestion().getId());
		
//		Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
		
		for(int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content, null);
		}
		
	}
}
