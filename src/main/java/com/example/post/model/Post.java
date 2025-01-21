package com.example.post.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Post {
	private Long id;			// 게시글 ID
	private String title;		// 제목
	private String content;		// 내용
	private String username;	// 작성자
	private String password;	// 비밀번호
	private int views;			// 조회수
	private LocalDateTime createTime;	// 작성일
	
	// 조회수 증가
	public void increamentViews() {
		this.views++;
	}
}
