package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	@ManyToMany
    Set<SiteUser> voter;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category; // 질문이 속한 카테고리

	public Question(String subject, String content, LocalDateTime createDate, Category category, SiteUser author) {
		this.subject = subject;
		this.content = content;
		this.createDate = createDate;
		this.category = category;
		this.author = author;
	}

	public void update(String subject, String content, LocalDateTime modifyDate, Category category) {
		this.subject = subject;
		this.content = content;
		this.modifyDate = modifyDate;
		this.category = category;
	}

	public void addVoter(SiteUser siteUser) {
		this.voter.add(siteUser);
	}
}
