package com.cos.blogapp.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.blogapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinReqDto {
	@Size(min = 2, max = 20)
	@NotBlank // 공백과 널을 막음
	private String username;
	@Size(min = 4, max = 20)
	@NotBlank // 공백과 널을 막음
	private String password;
	@Size(min = 4, max = 50)
	@NotBlank // 공백과 널을 막음
	private String email;
	
	public User toEntity() {
		User user = new User();
		user.setUsername( username);
		user.setPassword(password);
		user.setEmail(email);
		return user;
	}
}
