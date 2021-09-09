package com.cos.blogapp.domain.board;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.cos.blogapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//테이블 모델
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 테이블을 만듬 JPA
public class Board {
	@Id // primary
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스같은 것
	private int id; // PK(자동증가 번호)
	private String title; 
	@Lob // 데이터 타입 longtext(4GB)
	private String content;
	
	@JoinColumn(name = "userId") // FK이름 지정
	@ManyToOne(fetch = FetchType.EAGER) // FK
	private User user;
}