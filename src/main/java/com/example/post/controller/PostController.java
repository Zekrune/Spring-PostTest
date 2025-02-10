package com.example.post.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.example.post.Util.PageNavigator;
import com.example.post.model.posts.Post;
import com.example.post.model.posts.PostCreateDto;
import com.example.post.model.posts.PostUpdateDto;
import com.example.post.model.users.User;
import com.example.post.service.PostServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {
	@Value("${file.upload.path}")
	private String uploadPath;	// 파일 업로드 경로
	private final PostServiceImpl postService;
	
	// 한 페이지당 보여줄 게시글의 수
	@Value("${page.countPerPage}")
	private int countPerPage;
	
	// 한 그룹당 보여줄 페이지 수
	@Value("${page.pagePerGroup}")
	private int pagePerGroup;
	
	// 게시글 작성 페이지 이동
	@GetMapping("posts/create")
	public String createPostForm(
			// 세션에 저장된 데이터를 조회한다.
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			Model model) {
		// 사용자가 로그인을 했는지 체크
		log.info("loginUser: {}", loginUser);
		
		model.addAttribute("postCreateDto", new PostCreateDto());
				
		// 게시글 작성 페이지의 뷰 이름을 리턴
		return "posts/create";
	}
	
	// 게시글 등록
	@PostMapping("posts")
	public String savePost(
			@Validated @ModelAttribute PostCreateDto postCreateDto,
			BindingResult bindingResult,
			@SessionAttribute(name = "loginUser") User loginUser,
			@RequestParam(name = "file", required = false) MultipartFile file) throws IllegalStateException, IOException {
				
		log.info("file.getOriginalFilename(): {}", file.getOriginalFilename());
		log.info("file.getSize(): {}", file.getSize());
//		String uploadPath = "c:/Dev/uploads/";
//		// 파일을 업로드 경로에 저장
//		file.transferTo(new File(uploadPath + file.getOriginalFilename()));
						
		if (bindingResult.hasErrors()) {
			return "posts/create";
		}
		
		log.info("postCreateDto: {}", postCreateDto);
		
		Post post = postCreateDto.toEntity();
				
		// 세션에서 사용자 정보를 가져와서 Post 객체에 넣어준다.
		post.setUser(loginUser);
		
		Post savedPost = postService.savePost(post, file);
		log.info("savedPost: {}", savedPost);
		
		return "index";
	}
	
	// 게시글 목록 조회
	@GetMapping("posts")
	public String listPosts(
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			@RequestParam(name = "page", defaultValue = "0") int page,
			Model model) {
		
		if (page < 0) page = 0;
		Page<Post> posts = postService.getAllPosts(page, countPerPage);
		PageNavigator navi = new PageNavigator(countPerPage, 
												pagePerGroup, 
												posts.getTotalPages(), 
												posts.getNumber(), 
												posts.getTotalElements());
		
		model.addAttribute("posts", posts);
		model.addAttribute("navi", navi);
		
		return "posts/list";
	}
	
	// 게시글 조회
	@GetMapping("posts/{postId}")
	public String viewPost(
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			@PathVariable(name = "postId") Long postId,
			Model model) {
				
		Post findPost = postService.readPost(postId);
		model.addAttribute("post", findPost);
		
		return "posts/view";
	}
		
	// 게시글 삭제
	@GetMapping("posts/remove/{postId}")
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
		
		Post post = postService.getPostById(postId);
		PostUpdateDto postUpdateDto = post.toUpdateDto();		
		model.addAttribute("postUpdateDto", postUpdateDto);
		
		return "posts/edit";
	}
	
	// 게시글 수정
	@PostMapping("posts/edit/{postId}")
	public String editPost(
			@SessionAttribute(name = "loginUser") User loginUser,
			@PathVariable(name = "postId") Long postId,
			@ModelAttribute PostUpdateDto postUpdateDto) {
		log.info("updatePost: {}", postUpdateDto);
				
		// 수정 하려는 게시글이 존재하고 로그인 사용자와 게시글의 작성자가 같은지 확인
		Post post = postService.getPostById(postId);
		if (post.getUser().getId() == loginUser.getId()) {
			postService.updatePost(postId, postUpdateDto.toEntity());
		}
		
		return "redirect:/posts";
	}
	
	@GetMapping("posts/{postId}/download/{fileAttachmentId}")
	public ResponseEntity<Resource> download(
			@PathVariable(name = "postId") Long postId,
			@PathVariable(name = "fileAttachmentId") Long fileAttachmentId
			) throws MalformedURLException {
		Post post = postService.getPostById(postId);
		if (post.getFileAttachment() == null) {
			return ResponseEntity.notFound().build();
		}		
		// 첨부파일이 저장된 전체 경로값을 만든다.
		String fullPath = uploadPath + "/" + post.getFileAttachment().getStoredFilename();
		UrlResource resource = new UrlResource("file:" + fullPath);
		
		// 다운로드 되는 파일의 이름을 지정한다.
		String encodedFilename = UriUtils.encode(post.getFileAttachment().getOriginalFilename(), StandardCharsets.UTF_8);
		
		String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";
		
		return ResponseEntity.ok()
				.header("Content-Disposition", contentDisposition)
				.body(resource);
	}
	
}
