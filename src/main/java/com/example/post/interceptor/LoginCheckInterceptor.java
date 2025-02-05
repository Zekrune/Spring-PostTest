package com.example.post.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("로그인 체크 인터셉터 실행");
		// 사용자가 우너래 요청했던 URL
		String requestURI = request.getRequestURI();
		
		// 세션 정보를 가져온다.
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null) {
			//로그인을 하지 않은 경우 로그인 페이지로 리다이렉트
			log.info("로그인 하지 않는 사용자 입니다.");
			response.sendRedirect("/users/login?redirectURL=" + requestURI);
			return false;			
		}
		
		// 로그인 완료된 경우
		return true;
	}
}
