package com.example.post.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 인터셉터는 HandlerInteceptor 인터페이스를 구현한다.
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 컨트롤러 보다 먼저 실행
		log.info("preHandle 실행");
		
		String requestURI = request.getRequestURI();
		
		if (handler instanceof HandlerMethod) {
			// 호출할 컨트롤러의 메소드 정보를 담고 있다.
			 HandlerMethod handlerMethod = (HandlerMethod) handler;
			 log.info("handlerMethod: {}", handlerMethod);
		}
		
		log.info("requestURI: {}", requestURI);
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 컨트롤러의 핸들러 메소드가 정상적으로 실행된 후에 실행
		// 예외가 발생하면 호출되지 않는다.
		log.info("postHandle 실행");
		log.info("modelAndView: {}", modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 요청에 대한 응답이 완료 됐을 때 실행
		log.info("afterCompletion 실행");
		// 예외 발생과 관계없이 호출된다.
		if (ex != null) {
			log.error("afterCompletion 예외 발생!!");
		}
	}
}









