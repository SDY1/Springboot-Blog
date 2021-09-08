package com.cos.blogapp.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserControllerTest {

		@GetMapping("test/join")
		public @ResponseBody String testJoin() { // 데이터가 리턴할 수 있게 함
			return "test/join";
		}
		
		@GetMapping("test/login")
		public @ResponseBody String testLogin() { // 데이터가 리턴할 수 있게 함
			return "<script>alert('hello');</script>";
		}
		
		// 주소?name=홍길동
		// /test/data/2
		@GetMapping("/test/data/{num}")
		public @ResponseBody String testData(@PathVariable int num) {
			if(num == 1) { // 정상
				StringBuilder sb = new StringBuilder();
				sb.append("<script>");
				sb.append("location.href='/';");
				sb.append("</script>");
				return sb.toString();
			}else { // 오류
				return "오류가 났습니다"+num;				
			}
		}
}
