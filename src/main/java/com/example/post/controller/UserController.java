package com.example.post.controller;

import java.lang.System.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.post.model.User;
import com.example.post.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor // 롬복 생성자 주입 어노테이션
@Controller
public class UserController {
    
    private final UserService userService;

    // 회원가입 페이지 요청 처리
    @GetMapping(path = "register")
    public String register() {
        log.info("register");
        return "register";
    }
    
    // 회원가입 요청 처리
    @PostMapping(path = "register_v3")
    public String registerUser(
            @ModelAttribute User user) {
        
        log.info("user: {}", user);
        User registeredUser = userService.registerUser(user);
        log.info("registeredUser: {}", registeredUser);
        
        return "register_success";
    }
    
    // user-detail-v1/{userId} 사용하여 ID로 회원 정보 조회
    @GetMapping("user-detail-v1/{userId}")
    public String userDetailsV1(
            @PathVariable(name = "userId") Long id,
            Model model) {
        
        User user = userService.getUserById(id);
//		//검색한 User 정보를 Model에 담는다.
        model.addAttribute("user", user);
        
        return "user_detail";
    }
	
	// ID로 회원 정보 조회
	// ex) /user-details/{사용자ID} -> 정보를 조회하며 -> user_detail.html을 보여준다.
	// URL를 사용하는건 무조건 Get타입
    // user-detail-v2/{userId} 사용하여 ID로 회원 정보 조회
    @GetMapping("user-detail-v2/{userId}")
    public String userDetailsV2(
            @PathVariable(name = "userId") Long id,
            Model model) {
        
        User user = userService.getUserById(id);
		//검색한 User 정보를 Model에 담는다.
		model.addAttribute("user", user);
        
        return "user_detail";
    }
	
    // 사용자 목록 조회
    @GetMapping("user-list")
    public String userList(Model model) {
        // 모든 사용자 정보를 가져옵니다.
        List<User> users = userService.getAllUsers(); // service에서 getAllUsers 메서드 호출
        model.addAttribute("users", users); // users 정보를 모델에 담기
        return "user_list"; // user_list.html 페이지로 이동
    }
}
