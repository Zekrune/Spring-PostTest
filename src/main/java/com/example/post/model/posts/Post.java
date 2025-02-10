package com.example.post.model.posts;

import java.time.LocalDateTime;

import com.example.post.model.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Post {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;			// 게시글 ID
	private String title;		// 제목
	
	@Lob
	private String content;		// 내용
	
	@ManyToOne(fetch = FetchType.LAZY)	// 지연로딩으로 변경
	@JoinColumn(name = "user_id")
	private User user;			// 작성자
	private int views;			// 조회수
	private LocalDateTime createTime;	// 작성일
	
	@OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)	// mappedBy는 연관관계 주인의 필드명
	@ToString.Exclude
	private FileAttachment fileAttachment;
	
	// 조회수 증가
	public void incrementViews() {
		this.views++;	
	}
	
	public PostUpdateDto toUpdateDto() {
		return PostUpdateDto.builder()
				.id(this.id)
				.title(this.title)
				.content(this.content)
				.build();
	}
}
