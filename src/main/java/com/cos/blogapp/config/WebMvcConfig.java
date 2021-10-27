package com.cos.blogapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cos.blogapp.handler.SessionInterceptor;

// 스프링이 시작할때 WebMvcConfigurer타입을 찾아냄
@Configuration // IoC에 띄움
public class WebMvcConfig implements WebMvcConfigurer{
	public WebMvcConfig() {
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionInterceptor())
			.addPathPatterns("/api/**");
//		registry.addInterceptor(new SessionInterceptor())
//		.addPathPatterns("/api/**"); 하나 더 만들고 싶을 때 이거랑 클래스 생성 
	}
	
}
