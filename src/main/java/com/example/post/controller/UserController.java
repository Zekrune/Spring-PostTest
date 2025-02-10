package com.example.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.post.model.users.User;
import com.example.post.model.users.UserCreateDto;
import com.example.post.model.users.UserLoginDto;
import com.example.post.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor // 롬복 생성자 주입 어노테이션
@Controller
public class UserController {
	
private final UserService userService;

// 회원가입 페이지 요청 처리
@GetMapping(path = "users/register")
public String register(Model model) {
	UserCreateDto userCreateDto = new UserCreateDto();
	model.addAttribute("userCreateDto", userCreateDto);
	
	return "users/register";
}

// 회원가입 요청 처리
@PostMapping(path = "users/register")
public String registerUser(
		@Validated @ModelAttribute UserCreateDto userCreateDto,
		BindingResult bindingResult
		) {
	// 유효성 검증이 실패 했는지 확인
	if (bindingResult.hasErrors()) {
		log.info("유효성 검증 실패");
		return "users/register";
	}
	
	// username 중복 확인
	if (userService.getUserbyUsername(userCreateDto.getUsername()) != null) {
		// 이미 사용중인 username이 있다.
		// 에러코드는 errors.properties 파일에 정의된 에러 코드와 메시지를 사용한다.
		bindingResult.reject("duplicate.username");
		return "users/register";
	}
	
	log.info("user: {}", userCreateDto);
	
	// userCreateDto -> user 타입으로 변환
	User registedUser = userService.registerUser(userCreateDto.toEntity()); // 서비스 레이어를 통해 사용자 등록
	log.info("registedUser: {}", registedUser);
	
	return "redirect:/";
}
    
//로그인 페이지 이동
	@GetMapping("users/login")
	public String loginForm(Model model) {
		model.addAttribute("userLoginDto", new UserLoginDto());
		return "users/login";
	}
	
	// 로그인 
	@PostMapping("users/login") 
	public String login(
			@Validated @ModelAttribute UserLoginDto userLoginDto,
			BindingResult bindingResult,
			HttpServletRequest request,
			@RequestParam(name = "redirectURL", defaultValue = "/") String redirectURL) {
		
		log.info("redirectURL: {}", redirectURL);
		
		// 로그인 정보 검증에 실패하면 로그인 페이지로 돌아간다.
		if (bindingResult.hasErrors()) {
			return "users/login";
		}
		log.info("user: {}", userLoginDto);
		
		// username에 해당하는 User 객체를 찾는다.
		User findUser = userService.getUserbyUsername(userLoginDto.getUsername());
		log.info("findUser: {}", findUser);
		
		// 사용자가 입력한 username, password 정보가 데이터베이스의 User 정보와 일치하는지 확인
		if (findUser == null || !findUser.getPassword().equals(userLoginDto.getPassword())) {
			// 로그인 실패 시 로그인 페이지로 리다이렉트
			bindingResult.reject("loginFailed", "아이디 또는 패스워스가 다릅니다.");
			return "/users/login";
		}
		
		// Request 객체에 저장돼 있는 Session 객체를 받아온다.
		HttpSession session = request.getSession();
		// session 에 로그인 정보를 저장한다.
		session.setAttribute("loginUser", findUser);
			
		return "redirect:" + redirectURL;
	}
	
	// 로그아웃
	@GetMapping("users/logout")
	public String logout(HttpSession session) {
//		session.setAttribute("loginUser", null);
		// 세션의 데이터를 모두 삭제한다.
		session.invalidate();
		
		return "redirect:/";
	}
	
	
	// 세션 저장 확인
	@ResponseBody
	@GetMapping("loginCheck")
	public String loginCheck(HttpServletRequest request) {
		// Request 객체에 저장돼 있는 Session 객체를 받아온다.
		HttpSession session = request.getSession();
		// session 에 저장된 loginUsername 정보를 찾는다.
		String loginUsername = (String) session.getAttribute("loginUsername");
		log.info("loginUsername: {}", loginUsername);
		
		return "ok";
	}
	
	
}
