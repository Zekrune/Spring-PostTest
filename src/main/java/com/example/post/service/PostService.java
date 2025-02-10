package com.example.post.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.post.model.posts.Post;
import com.example.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

public interface PostService {
    // 글 저장
	Post savePost(Post post, MultipartFile file);
	
	// 글 전체 조회
	public Page<Post> getAllPosts(int page, int size);

	// 아이디로 글 조회
	public Post getPostById(Long postId);
	
	// 글 읽기
	public Post readPost(Long postId);
	
	// 글 삭제
	public void removePost(Long postId);
	
	// 글 수정
	public void updatePost(Long postId, Post updatePost);
	
}
