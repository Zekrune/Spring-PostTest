package com.example.post.controller;

import java.util.List; 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.post.model.posts.Post;
import com.example.post.model.users.User;
import com.example.post.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;


@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {
	private final PostService postService;
	
	// 게시글 작성 페이지 이동
	@GetMapping("posts/create")
	public String createPostForm(
			// 세션의 저장된 데이터를 조회한다.
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {
		// 사용자가 로그인을 했는지 체크
		log.info("loginUser: {}", loginUser);
		if (loginUser == null) {
			// 로그인을 하지 않았으면 로그인 페이지로 리다이렉트
			return "redirect:/users/login";
		}
		
		// 게시글 작성 페이지의 뷰 이름을 리턴
		return "posts/create";
	}
	
	// 게시글 등록
	@PostMapping("posts")
	public String savePost(
			@ModelAttribute Post post,
			@SessionAttribute(name = "loginUser") User loginUser) {
		log.info("post: {}", post);
		// 세션에서 사용자 정보를 가져와서 post 객체에 넣어준다.
		post.setUser(loginUser);
		Post savedPost = postService.savePost(post);
		
		log.info("savedPost: {}", savedPost);
		
//		redirect 사용이유 찾기
		return "redirect:/posts";
	}
	
	// 게시글 목록 조회
	@GetMapping("posts")
	public String listPosts(
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			Model model) {
		
		// 로그인 하지 않음
		if (loginUser == null) {
			return "redirect:/users/login";
		}
		
		List<Post> posts = postService.getAllPosts();
		model.addAttribute("posts", posts);
		
		return "posts/list";
	}
	
	// 게시글 조회
	@GetMapping("posts/{postId}")
    public String viewPost(
    		@PathVariable(name = "postId") Long postId,
    		Model model) {
		Post findPost = postService.getPostById(postId);
		model.addAttribute("post", findPost);
		
		return "posts/view";
    }
	
	// 게시글 삭제
	@GetMapping("/posts/remove/{postId}")
	public String removePost(
			@SessionAttribute(name = "loginUser") User loginUser,
			@PathVariable(name = "postId") Long postId) {
		
		// 삭제하려고 하는 게시글이 로그인 사용자가 작성한 글인지 확인
		Post findPost = postService.getPostById(postId);
		// 로그인 사용자와 작성자가 다르면 삭제하지 않고 목록 페이지로 리다이렉트 한다.
		if (findPost == null || findPost.getUser().getId() != loginUser.getId()) {
			return "redirect:/posts";
		}
		
		postService.removePost(postId);
		
		return "redirect:/posts";
	}
	
	// 게시글 수정 페이지 이동
	@GetMapping("posts/edit/{postId}")
	public String editPostForm(
			@PathVariable(name = "postId") Long postId,
			Model model) {
		model.addAttribute("post", postService.getPostById(postId));
		return "posts/edit";
	}
	
	//게시글 수정
	@PostMapping("posts/edit/{postId}")
	public String editPost(
			@SessionAttribute(name = "loginUser") User loginUser,
			@PathVariable(name = "postId") Long postId,
			@ModelAttribute Post updatePost) {
		log.info("updatePost: {}", updatePost);
		
		// 수정 하려는 게시글이 존재하고 로그인 사용자와 게시글의 작성자가 같은지 확인
		Post post = postService.getPostById(postId);
		if (post.getUser().getId() == loginUser.getId()) {
			postService.updatePost(postId, updatePost);
		}
		
		return "redirect:/posts";
	}
}
