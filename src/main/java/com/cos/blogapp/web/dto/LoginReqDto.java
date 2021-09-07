package com.cos.blogapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO = Data Transfer Object(데이터 전송 오브젝트)
@Data  // getter / setter
@AllArgsConstructor  // 모든필드사용생성자
@NoArgsConstructor  // 기본생성자
public class LoginReqDto {
	private String username;
	private String password;
	
//	public static void main(String[] args) {
//		LoginReqDto dto = new LoginReqDto();
//	}
}
