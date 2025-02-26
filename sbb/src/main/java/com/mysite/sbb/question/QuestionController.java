package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@RequestMapping("/question")
// prefix를 /question로 설정
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final QuestionRepository questionRepository;
	private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;
    private final CategoryService categoryService;
	
	@GetMapping("/")
	public String root() {
		return "redirect:question/list";
	}
	
	@GetMapping("question/list")
	/* @ResponseBody */
	public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "categoryId", required = false) Long categoryId) {
        Page<Question> paging;
        if (categoryId != null) {
            paging = this.questionService.getListByCategory(page, kw, categoryId);
        } else {
            paging = this.questionService.getList(page, kw);
        }
        List<Category> categories = categoryService.getAllCategories(); // 카테고리 목록
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", categoryId);
        return "question_list";
	}

	@GetMapping(value = "question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,
                         @PageableDefault(size = 3) Pageable pageable,
                         @RequestParam(value = "sort", defaultValue = "createDate,desc") String sort,
                         AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        Page<?> answerPaging = this.answerService.getAnswersByQuestion(id, pageable, sort);

        model.addAttribute("question", question);
        model.addAttribute("answerPaging", answerPaging);
        model.addAttribute("sort", sort);
        return "question_detail";
    }

	@PreAuthorize("isAuthenticated()")
	@GetMapping("question/create")
    public String questionCreate(Model model, QuestionForm questionForm) {
        model.addAttribute("categories", categoryService.getAllCategories()); // 카테고리 목록 전달
        return "question_form";
    }
	@PreAuthorize("isAuthenticated()")
	@PostMapping("question/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
    		Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(
                questionForm.getSubject(),
                questionForm.getContent(),
                questionForm.getCategoryId(),
                siteUser
        );
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }
	
	
	// 수정 권한이 있으면 질문 수정 폼에 제목, 내용 표시 
	@PreAuthorize("isAuthenticated()")
    @GetMapping("question/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) { // 로그인한 사용자와 질문의 작성자가 같지 않으면 수정 불가
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        questionForm.setCategoryId(question.getCategory().getId());
        return "question_form";
    }
	
	// 질문 수정 기능
	@PreAuthorize("isAuthenticated()")
    @PostMapping("question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), questionForm.getCategoryId());
        return String.format("redirect:/question/detail/%s", id);
    }
	
	// 삭제 권한이 있으면 질문 삭제
	@PreAuthorize("isAuthenticated()")
    @GetMapping("question/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) { // 로그인한 사용자와 질문의 작성자가 같지 않으면 삭제 불가
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
	
	 @PreAuthorize("isAuthenticated()")
	    @GetMapping("question/vote/{id}")
	    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
	        Question question = this.questionService.getQuestion(id);
	        SiteUser siteUser = this.userService.getUser(principal.getName());
	        this.questionService.vote(question, siteUser);
	        return String.format("redirect:/question/detail/%s", id);
	    }
}
