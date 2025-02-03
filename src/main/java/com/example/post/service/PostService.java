package com.example.post.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.post.model.posts.Post;
import com.example.post.repository.PostRepository;


import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true) // 읽기전용으로 가져온다.
@RequiredArgsConstructor
@Service
public class PostService {
	//PostService 객체 생성 시점에 스프링 컨테이너가
	// 자동으로 의존성을 주입(Dependency Injection) 해준다.
	private final PostRepository postRepository;
	// 글 저장
	
	@Transactional
	public Post savePost(Post post) {
		post.setCreateTime(LocalDateTime.now());
		postRepository.save(post);
		return post;
	}
	
	// 글 전체 조회 (Post에 List 형식으로)
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}
	
	// 게시글 읽기
	@Transactional
	public Post readPost(Long postId) {
		Post post = getPostById(postId);
		post.increamentViews();
		
		return post;
		}
	
	// 아이디로 글 조회
	@Transactional
	public Post getPostById(Long postId) {
		Optional<Post> findPost = postRepository.findById(postId);
//		if (findPost.isPresent()) {
//			// 조회 수 증가
//			Post post = findPost.get();
//			Post.increamentViews();
//			return Post;
//		}
//		throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		
		Post post = findPost.orElseThrow(
				() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
		
		return post;
	}
	
	// 글 삭제
	@Transactional
	public void removePost(Long postId) {
			Post post = getPostById(postId);
			postRepository.delete(post);
		}
	
	// 글 수정
	@Transactional
	public void updatePost(Long postId, Post updatePost) {
			Post post = getPostById(postId);
			post.setTitle(updatePost.getTitle());
			post.setContent(updatePost.getContent());
		}
	}