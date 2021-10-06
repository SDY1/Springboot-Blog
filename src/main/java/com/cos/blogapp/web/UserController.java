package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.service.UserService;
import com.cos.blogapp.util.MyAlgorithm;
import com.cos.blogapp.util.SHA;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.JoinReqDto;
import com.cos.blogapp.web.dto.LoginReqDto;
import com.cos.blogapp.web.dto.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 꼭 필요한 것을 생성 -> final붙여서 접근 가능
public class UserController {
	// @Autowired 자동주입
	final private HttpSession session;
	final private UserService userService;
	// DI
//	public UserController(UserRepository userRepository, HttpSession session) {
//		this.userRepository = userRepository;
//		this.session = session;
//	}
	
	@PutMapping("/user/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody UserUpdateDto dto, BindingResult bindingResult) {
		// 유효성
		if(bindingResult.hasErrors()) {  // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		// 인증
		User principal = (User)session.getAttribute("principal");
		if(principal == null) { // 로그인 안됨
			throw new MyAsyncNotFoundException("인증이 되지 않았습니다");
		} 
		
		// 권한
		if(principal.getId() != id) {
			throw new MyAsyncNotFoundException("회원정보를 수정할 권한이 없습니다");
		}
		userService.회원수정(principal, dto);
		
		// 세션 동기화 해주는 부분
		principal.setEmail(dto.getEmail());
		session.setAttribute("principal", principal); // 세션 값 변경
		
		return new CMRespDto<>(1, "성공", null);
	}	
	
	@GetMapping("/user/{id}")
	public String userInfo(@PathVariable int id) {
		// 기본은 userRepository.findById(id) 디비에서 가져와야 함
		// 편법은 세션값을 가져올 수도 있음
		
		return "user/updateForm";
	}
	@GetMapping("/logout")
	public String logout() {
		session.invalidate(); // 세션에 있는 모든 값 무효화(jsessionId에 있는 값 비우는 것)
		return "redirect:/";
	}
	// http://localhost:8080/login -> login.jsp
	// views/user/login.jsp
	@GetMapping("/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@PostMapping("/login") // name값만 파싱해줌
	// getter / setter에 자동으로 넣어줌
	public @ResponseBody String login(@Valid LoginReqDto dto, BindingResult bindingResult) {
//		1. username, password 받기
		System.out.println(dto.getUsername());
		System.out.println(dto.getPassword());
		// validation
		System.out.println("에러사이즈: " + bindingResult.getFieldErrors().size());
		if(bindingResult.hasErrors()) {  // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("필드: " + error.getField());
				System.out.println("메시지: " + error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}

//		2. DB -> 조회
		User userEntity = userService.로그인(dto);
		if(userEntity == null) {
//			return "redirect:/loginForm";
			return Script.back("아이디 혹은 비밀번호를 잘못 입력하였습니다");
		}else {
			// 세션 날라가는 조건: 1. session.invaildate(), 2. 브라우저를 닫으면 날라감
			session.setAttribute("principal", userEntity);  // principal:인증주체
//			return "redirect:/"; // 만들어 놨으면 함수로 사용해라
			return Script.href("/","로그인 성공");
		}
	}
	
	@PostMapping("/join")
	// user를 안만드는 이유가 필요없는 값은 안받기 때문에 dto받음
	// 리플렉션
	public @ResponseBody String join(@Valid JoinReqDto dto, BindingResult bindingResult) { // username=love&password=1234&email=love@nate.com 
//		User user = new User(); // 생성자 사용해도 됨
//		user.setUsername(dto.getUsername());
//		user.setPassword(dto.getPassword());
//		user.setEmail(dto.getEmail());		
		// 1. 유효성 검사 실패 - 자바스크립트 응답(경고창, 뒤로가기)
		// 2. 정상 - 로그인 페이지
//		System.out.println("에러사이즈: " + bindingResult.getFieldErrors().size());
		if(bindingResult.hasErrors()) {  // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("필드: " + error.getField());
				System.out.println("메시지: " + error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		
		userService.회원가입(dto);
		
		return Script.href("/loginForm");
	}
}
