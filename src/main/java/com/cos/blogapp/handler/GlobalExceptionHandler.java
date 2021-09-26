package com.cos.blogapp.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;

//1. 익셉션 핸들링(디스패쳐서블릿으로 던져진 예외 처리) 2. @Controller의 역할
@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 어떤 익셉션이 발생했는지
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
//		System.out.println("오류 터짐" + e.getMessage());
		return Script.href("/", e.getMessage());
	}
	
	@ExceptionHandler(value = MyAsyncNotFoundException.class)
	public @ResponseBody CMRespDto<String> error2(MyAsyncNotFoundException e) {
//		System.out.println("오류 터짐" + e.getMessage());
		return new CMRespDto<String>(-1, e.getMessage(), null); // fetch나 ajex같은 비동기요청은 json데이터응답 -> (자바스크립트에서)부분 리로드
							 // 일반적 http 요청은  html응답 -> 전체 리로드
		                     // 여기서는 text형식으로 응답
	}
}
