package com.cos.blogapp.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

// Board타입 지네릭을 선언했기 때문에 User는 매핑 안됨
public interface BoardRepository extends JpaRepository<Board, Integer>{

}
