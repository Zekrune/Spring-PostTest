package com.example.post.model.posts;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUpdateDto {
	private Long id;
	private String title;		// 제목
	private String content;		// 내용
		
	public Post toEntity() {
		return Post.builder()
				.id(this.id)
				.title(this.title)
				.content(this.content)
				.build();
	}
}
