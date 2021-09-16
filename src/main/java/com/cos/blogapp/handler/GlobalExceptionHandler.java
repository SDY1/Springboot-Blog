package com.cos.blogapp.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;

//1. 익셉션 핸들링(디스패쳐서블릿으로 던져진 예외 처리) 2. @Controller의 역할
@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 어떤 익셉션이 발생했는지
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
//		System.out.println("오류 터짐" + e.getMessage());
		return Script.href("/", e.getMessage());
	}
}
