package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.service.BoardService;
import com.cos.blogapp.service.CommentService;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // 컴퍼넌트 스캔(스프링) IoC
public class BoardController {
	private final BoardService boardService;
	private final CommentService commentService;
	private final HttpSession session;

	@PostMapping("/board/{boardId}/comment")
	public String commentSave(@PathVariable int boardId, CommentSaveReqDto dto) {
		User principal = (User) session.getAttribute("principal");
		if (principal == null) { // 로그인 안됨
			throw new MyNotFoundException("인증이 되지 않았습니다");
		}
		commentService.댓글등록(boardId, dto, principal);
		
		return "redirect:/board/" + boardId;
	}

	@PutMapping("/board/{id}") // JSON을 JAVA로 받아줌 // 무조건 바인딩리절트는 dto옆에!
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody BoardSaveReqDto dto,
			BindingResult bindingResult) {
		User principal = (User) session.getAttribute("principal");
		// 이러한 공통로직을 AOP처리로 따로 빼면 좋음(인증, 권한, 유효성 검사)
		// 인증
		if (principal == null) { // 로그인 안됨
			throw new MyAsyncNotFoundException("인증이 되지 않았습니다");
		}

		// 유효성 검사
		if (bindingResult.hasErrors()) { // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		boardService.게시글수정(id, principal, dto);
		return new CMRespDto<>(1, "업데이트 성공", null);
	}

	// 모델의 접근을 안하면 인증/권한 굳이 필요없음(수정에서만 막아주면 됨)
	@GetMapping("/board/{id}/updateForm")
	public String boardUpdateForm(@PathVariable int id, Model model) {
		Board boardEntity = boardService.게시글수정페이지이동(id);
		model.addAttribute("boardEntity", boardEntity); // 클라이언트에서 응답되면 데이터 사라짐
		return "board/updateForm";
	}

	// API(AJAX)요청
	@DeleteMapping("/board/{id}")
	public @ResponseBody CMRespDto<String> deleteById(@PathVariable int id) {
		// 인증이 된 사람만 함수 접근 가능(로그인 된 사람)
		User principal = (User) session.getAttribute("principal");
		if (principal == null) {
			throw new MyAsyncNotFoundException("인증이 되지 않았습니다");
		}
		
		boardService.게시글삭제(id, principal);
		
		return new CMRespDto<String>(1, "성공", null); // 데이터리턴 String -> text/plain
	}

	// 쿼리스트링, 패스var -> 디비 where에 걸리는 친구들
	// 1. 컨트롤러 선정 2. HttpMethod 선정 3. 받을 데이터가 있는지(body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야하면 Model접근하기 orElse Model에 접근할 필요가 없음
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// Board 객체에 존재하는 것(Board(o), User(o), List<Comment>(x))
		Board boardEntity = boardService.게시글상세보기(id);
		model.addAttribute("boardEntity", boardEntity);
		return "board/detail"; // ViewResolver
	}

	@PostMapping("/board") // 보드 모델에 저장할 것임
	// x-www-form-urlencoded 이 타입만 받을 수 있음
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		// 공통 로직 시작---------------------------------------------------
		User principal = (User) session.getAttribute("principal");

		// 인증체크
		if (principal == null) { // 로그인 안됨
//			return Script.back("잘못된 접근입니다");
			return Script.href("/loginForm", "잘못된 접근입니다");
		} // postman에서 접근 불가(로그인 인증해야 되서)

		if (bindingResult.hasErrors()) { // 에러가 터졌을 때
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		// 공통 로직 끝--------------------------------------------
		dto.setContent(dto.getContent().replaceAll("<p>", ""));
		dto.setContent(dto.getContent().replaceAll("</p>", "")); // p태그 날리기
		
		// 핵심 로직 시작------------------------------
		boardService.게시글등록(dto, principal);
		// 핵심 로직 끝----------------------------
//		return "redirect:/";
		return Script.href("/", "글쓰기 성공");
	}

	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}

	@GetMapping("/board") // 보드 모델에서 가져올 것임
	public String home(Model model, int page) {
	
		model.addAttribute("boardsEntity", boardService.게시글목록보기(page));
//		System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}
