package com.cos.blogapp.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO = Data Transfer Object(데이터 전송 오브젝트)
@Data  // getter / setter
@AllArgsConstructor  // 모든필드사용생성자
@NoArgsConstructor  // 기본생성자
public class LoginReqDto {
	@Size(min = 2, max = 20)
	@NotBlank // 공백과 널을 막음
	private String username;
	@Size(min = 4, max = 20)
	@NotBlank // 공백과 널을 막음
	private String password;
	
//	public static void main(String[] args) {
//		LoginReqDto dto = new LoginReqDto();
//	}
}
