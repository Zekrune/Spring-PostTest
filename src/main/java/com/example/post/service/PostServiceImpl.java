package com.example.post.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.post.Util.FileAttachmentUtil;
import com.example.post.model.posts.FileAttachment;
import com.example.post.model.posts.Post;
import com.example.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true) // 읽기 전용으로 가져온다.
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
	@Value("${file.upload.path}")
	private String uploadPath;	// 파일 업로드 경로
	
	// PostService 객체 생성 시점에 스프링 컨테이너가 
	// 자동으로 의존성을 주입(Dependency Injection) 해준다.
	private final PostRepository postRepository;

	// 글 저장
	@Transactional
	@Override
	public Post savePost(Post post, MultipartFile file) {		
		// 첨부 파일이 존재하는지 확인
		if (file != null && !file.isEmpty()) {
			// 첨부파일을 저장
			String storedFilename = FileAttachmentUtil.uploadFile(file, uploadPath);
			FileAttachment fileAttachment = new FileAttachment();
			fileAttachment.setOriginalFilename(file.getOriginalFilename());
			fileAttachment.setStoredFilename(storedFilename);
			fileAttachment.setSize(file.getSize());
			fileAttachment.setPost(post);
			post.setFileAttachment(fileAttachment);
		}
		
		post.setCreateTime(LocalDateTime.now());
		postRepository.save(post);
		
		return post;
	}
	
	// 글 전체 조회
	@Override
	public Page<Post> getAllPosts(int page, int size) {
//		return postRepository.findAll();
//		return postRepository.findAllPosts();
		Pageable pageable = PageRequest.of(page, size);
		return postRepository.findAllByOrderByCreateTimeDesc(pageable);
	}
	
	// 게시글 읽기
	@Transactional
	@Override
	public Post readPost(Long postId) {
		Post post = getPostById(postId);
		post.incrementViews();
		
		return post;
	}
	
	// 아이디로 글 조회
	@Override
	public Post getPostById(Long postId) {
		Optional<Post> findPost = postRepository.findById(postId);
//		if (findPost.isPresent()) {
//			// 조회 수 증가
//			Post post = findPost.get();
//			post.incrementViews();
//			return post;			
//		}
//		throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		
		Post post = findPost.orElseThrow(
				() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
		
		return post;
	}
	
	// 글 삭제
	@Transactional
	@Override
	public void removePost(Long postId) {
		Post post = getPostById(postId);
		postRepository.delete(post);
		
	}
	
	// 글 수정
	@Transactional
	@Override
	public void updatePost(Long postId, Post updatePost) {
		Post post = getPostById(postId);
		post.setTitle(updatePost.getTitle());
		post.setContent(updatePost.getContent());		
	}
	
}
