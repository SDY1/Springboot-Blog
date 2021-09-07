package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.web.dto.JoinReqDto;
import com.cos.blogapp.web.dto.LoginReqDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 꼭 필요한 것을 생성 -> final붙여서 접근 가능
public class UserController {
	// @Autowired 자동주입
	final private UserRepository userRepository;
	final private HttpSession session;
	// DI
//	public UserController(UserRepository userRepository, HttpSession session) {
//		this.userRepository = userRepository;
//		this.session = session;
//	}
	
	
	@GetMapping({"/" , "/home"})
	public String home() {
		return "home"; // 파일을 리턴함
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
	public String login(@Valid LoginReqDto dto, BindingResult bindingResult, Model model) {
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
			model.addAttribute("errorMap", errorMap);
			return "error/error";
		}
//		2. DB -> 조회
		User userEntity = userRepository.mLogin(dto.getUsername(), dto.getPassword());
		if(userEntity == null) {
			return "redirect:/loginForm";
		}else {
			session.setAttribute("principal", userEntity);  // principal:인증주체
			return "redirect:/"; // 만들어 놨으면 함수로 사용해라
		}
	}
	
	@PostMapping("/join")
	// user를 안만드는 이유가 필요없는 값은 안받기 때문에 dto받음
	// 리플렉션
	public String join(@Valid JoinReqDto dto, BindingResult bindingResult, Model model) { // username=love&password=1234&email=love@nate.com 
//		User user = new User(); // 생성자 사용해도 됨
//		user.setUsername(dto.getUsername());
//		user.setPassword(dto.getPassword());
//		user.setEmail(dto.getEmail());		
		System.out.println("에러사이즈: " + bindingResult.getFieldErrors().size());
		if(bindingResult.hasErrors()) {  // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("필드: " + error.getField());
				System.out.println("메시지: " + error.getDefaultMessage());
			}
			model.addAttribute("errorMap", errorMap);
			return "error/error";
		}
		userRepository.save(dto.toEntity());
		
		return "redirect:/loginForm"; // 리다이렉션(300)
	}
}
