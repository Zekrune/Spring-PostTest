package com.example.post.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.post.filter.LogFilter;
import com.example.post.filter.LoginCheckFilter;
import com.example.post.interceptor.LogInterceptor;
import com.example.post.interceptor.LoginCheckInterceptor;

import jakarta.servlet.Filter;

// 스프링 설정과 관련된 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	// @Configuration 클래스의 @Bean 메소드의 리턴 값이 스프링 빈으로 등록 된다.
//	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		// 사용할 필터를 지정한다.
		filterRegistrationBean.setFilter(new LogFilter());
		//필터의 순서를 지정한다. 숫자가 낮을수록 먼저 동작한다.
		filterRegistrationBean.setOrder(1);
		// 필터를 적용할 URL 패턴을 지정한다.
		filterRegistrationBean.addUrlPatterns("/*");   //전체 설정
//		filterRegistrationBean.addUrlPatterns("/users/*"); //특정 URL에서만
		return filterRegistrationBean;
	}
//	@Bean
	public FilterRegistrationBean loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		// 사용할 필터를 지정한다.
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		//필터의 순서를 지정한다. 숫자가 낮을수록 먼저 동작한다.
		filterRegistrationBean.setOrder(2);
		// 필터를 적용할 URL 패턴을 지정한다.
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new LogInterceptor())
//		// 인터셉터의 실행 순서
//		.order(1)
//		// 인터셉터를 적용할 URL 패턴을 지정   필터는("/*") 이터셉터는 ("/**")  별 갯수 차이
//		.addPathPatterns("/**");
		
//		registry.addInterceptor(new LoginCheckInterceptor())
//		.order(2)
//		.addPathPatterns("/**")
//		//인터셉트에서 제외할 URL
//		.excludePathPatterns("/", "/users/register", "/users/login", "/users/logout");
	}
}
