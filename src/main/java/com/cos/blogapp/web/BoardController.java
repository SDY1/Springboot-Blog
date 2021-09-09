package com.cos.blogapp.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // 컴퍼넌트 스캔(스프링) IoC
public class BoardController {
	private final BoardRepository boardRepository;
	
	@GetMapping("/board")
	public String home (Model model, int page) {		
		PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));
		     
		Page<Board> boardsEntity = boardRepository.findAll(pageRequest);
		// 영속화된 오브젝트 boardsEntity
//		List<Board> boardsEntity = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		// 지연로딩(lazy)은 board만 select하지만 영속화된 오브젝트(boardsEntity)에서 쓸 때 User을 select함
		// eager은 미리 땡겨옴 -> 둘다 select(default)
		// 한 건만 select할 때는 eager, 여러 건을 select하면 lazy(부하 때문)
		
		model.addAttribute("boardsEntity",boardsEntity);
//		System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}
