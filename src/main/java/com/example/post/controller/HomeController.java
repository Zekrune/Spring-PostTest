package com.example.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		log.info("home controller");
//		throw new RuntimeException("강제로 발생시킨 예외");
		return	"index";
	}
}
