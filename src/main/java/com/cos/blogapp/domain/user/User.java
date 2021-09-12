package com.cos.blogapp.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 테이블 모델
@AllArgsConstructor // 모든필드사용생성자
@NoArgsConstructor // 기본생성자
@Data // getter / setter
@Entity // 테이블을 만듬 JPA
public class User {
	@Id // primary
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스같은 것
	private int id; // PK(자동증가 번호)
	@Column(nullable = false, length = 20, unique = true)
	private String username; // 아이디
	@Column(nullable = false, length = 70)
	private String password;
	@Column(nullable = false, length = 50)
	private String email;
}
