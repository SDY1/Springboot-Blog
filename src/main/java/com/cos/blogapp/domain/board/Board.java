package com.cos.blogapp.domain.board;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.cos.blogapp.domain.comment.Comment;
import com.cos.blogapp.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//테이블 모델
@AllArgsConstructor
@NoArgsConstructor
@Data // getter, setter, toString
@Entity // 테이블을 만듬 JPA
public class Board {
	@Id // primary
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스같은 것
	private int id; // PK(자동증가 번호)
	@Column(nullable = false, length = 50)
	private String title; 
	@Lob // 데이터 타입 longtext(4GB)
	private String content;
	
	@JoinColumn(name = "userId") // FK이름 지정
	@ManyToOne(fetch = FetchType.EAGER) // FK
	private User user;
	
	// 양방향 매핑
	// mappedBy는 FK의 주인의 변수이름을 추가함
	@JsonIgnoreProperties({"board"}) // comments 객체 내부의 필드를 제외 시키는 법(무한루프 해결) - 뷰리졸브사용할 거라 필요는 없음(데이터리턴시에만 필요)
//	@JsonIgnore // comments자체를 제외
	@OneToMany(mappedBy = "board",fetch=FetchType.LAZY) // 이거는 원자성이 깨지기 때문에 테이블 안만들어짐. 셀렉트 용도로만 사용
	@OrderBy("id desc")
	private List<Comment> comments;
	
	
}
