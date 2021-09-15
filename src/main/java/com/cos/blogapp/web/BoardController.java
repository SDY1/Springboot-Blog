package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // 컴퍼넌트 스캔(스프링) IoC
public class BoardController {
	private final BoardRepository boardRepository;
	private final HttpSession session;
	
	//  쿼리스트링, 패스var -> 디비 where에 걸리는 친구들
	// 1. 컨트롤러 선정 2. HttpMethod 선정 3. 받을 데이터가 있는지(body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야하면 Model접근하기 orElse Model에 접근할 필요가 없음
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// select * from board where id =:id
		Board boardEntity = boardRepository.findById(id).get();
		model.addAttribute("boardEntity", boardEntity);
		return "board/detail";
	}
	
	@PostMapping("/board")// 보드 모델에 저장할 것임
	// x-www-form-urlencoded 이 타입만 받을 수 있음
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		User principal = (User)session.getAttribute("principal");
		
		// 인증체크
		if(principal == null) { // 로그인 안됨
//			return Script.back("잘못된 접근입니다");
			return Script.href("/loginForm", "잘못된 접근입니다");
		} // postman에서 접근 불가(로그인 인증해야 되서)
		
		if(bindingResult.hasErrors()) {  // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		dto.setContent(dto.getContent().replaceAll("<p>", ""));
		dto.setContent(dto.getContent().replaceAll("</p>", "")); // p태그 날리기
//		User user = new User();
//		user.setId(3);
//		boardRepository.save(dto.toEntity(user));		
		
		boardRepository.save(dto.toEntity(principal));
//		return "redirect:/";
		return Script.href("/", "글쓰기 성공");
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
	
		return "board/saveForm";
	}
	
	@GetMapping("/board") // 보드 모델에서 가져올 것임
	public String home (Model model, int page) {		
		// 페이징 & 정렬	
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
