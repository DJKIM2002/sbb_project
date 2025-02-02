package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// final이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
@Service
public class QuestionService {
	private final QuestionRepository questionRepository; // 생성자 방식 주입(@RequiredArgsConstructor)
    private final CategoryService categoryService;

	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
    	Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//    	Specification<Question> spec = search(kw);
    	return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    
    public Question getQuestion(Integer id) {  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, Long categoryId, SiteUser user) {
        Category category = categoryService.getCategoryById(categoryId);
        Question question = new Question(subject, content, LocalDateTime.now(), category, user);
        this.questionRepository.save(question);
    }

    public void modify(Question question, String subject, String content, Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        question.update(subject, content, LocalDateTime.now(), category);
        this.questionRepository.save(question);
    }
    
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    
    public void vote(Question question, SiteUser siteUser) {
        question.addVoter(siteUser);
        this.questionRepository.save(question);
    }

    public Page<Question> getListByCategory(int page, String kw, Long categoryId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 카테고리가 존재하는 경우 해당 카테고리의 질문만 필터링
        Category category = categoryService.getCategoryById(categoryId);
        return this.questionRepository.findByCategoryAndKeyword(category, kw, pageable);
    }


    // 게시글 검색 기능(Specification 이용)
//    private Specification<Question> search(String kw) {
//        return new Specification<>() {
//            private static final long serialVersionUID = 1L;
//            @Override
//            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);  // 중복을 제거 
//                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
//                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
//                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
//                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
//                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
//                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
//                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
//                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
//            }
//        };
//    }
}
