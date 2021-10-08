package com.cos.blogapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.comment.Comment;
import com.cos.blogapp.domain.comment.CommentRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final BoardRepository boardRepository;

	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글수정(int id, User principal, BoardSaveReqDto dto) {
		// 권한
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyAsyncNotFoundException("해당 게시글을 찾을 수 없습니다"));
		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 게시글의 주인이 아닙니다");
		}

//		Board board = dto.toEntity(principal);
//		board.setId(id); // update의 핵심
//
//		boardRepository.save(board);
		// 영속화된 데이터를 변경하면!
		boardEntity.setTitle(dto.getTitle());
		boardEntity.setContent(dto.getContent());
	}  // 트랜젝션 종료(더티체킹)

	// 이거는 여기있다고 좋아지는 건 없지만 형식상 이렇게 해줌
	public Board 게시글수정페이지이동(int id) {
		// 게시글 정보를 가지고 가야함
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyNotFoundException(id + "번호의 게시글을 찾을 수 없습니다"));
		return boardEntity;
	}

	// 트랜잭션 어노테이션 (트랜잭션을 시작하는 것)
	// rollbackFor (함수내부에 하나의 write라도 실패하면 전체를 rollback 하는 것)
	// 주의 : RuntimeException을 던져야 동작한다.
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글삭제(int id, User principal) {
		// 권한이 있는 사람만 함수 접근 가능(principal.id == {id})
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyAsyncNotFoundException("해당글을 찾을 수 없습니다"));

		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당글을 삭제할 권한이 없습니다");
		}

		try {
			boardRepository.deleteById(id); // 오류발생??(id가 없으면)
		} catch (Exception e) {
			throw new MyAsyncNotFoundException(id + "를 찾을 수 없어서 삭제할 수 없어요");
		}

	}

	public Board 게시글상세보기(int id) {
		// select * from board where id =:id

		// 1. orElse는 값을 찾으면 Board가 리턴, 못찾으면(괄호 안 내용 리턴)
//		Board boardEntity = boardRepository.findById(id)
//				.orElse(new Board(100, "글없어요", "글없어요", null));

		// 2. orElseThrow
		Board boardEntity = boardRepository.findById(id)
//				.orElseThrow(new Supplier<MyNotFoundException>() { // 어떤 익셉션이 발생하든 내가 정의한 익셉션으로 던져줄 수 있음
//					@Override
//					public MyNotFoundException get() {
//						return new MyNotFoundException(id + "를 찾을 수 없습니다");
//					}
//				}); 
				// 람다식으로 변형
				.orElseThrow(() -> new MyNotFoundException(id + "를 찾을 수 없습니다")); // 중괄호 안넣으면 무조건 리턴(한줄 시)
		return boardEntity;
	}

	@Transactional(rollbackFor = MyNotFoundException.class)
	public void 게시글등록(BoardSaveReqDto dto, User principal) {
//		User user = new User();
//		user.setId(3);
//		boardRepository.save(dto.toEntity(user));		

		boardRepository.save(dto.toEntity(principal));
	}

	public Page<Board> 게시글목록보기(int page) {
		// 페이징 & 정렬
		// 이거는 오래 안걸려서 여기 나둬도 됨
		PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));

		Page<Board> boardsEntity = boardRepository.findAll(pageRequest);
		// 영속화된 오브젝트 boardsEntity
//		List<Board> boardsEntity = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		// 지연로딩(lazy)은 board만 select하지만 영속화된 오브젝트(boardsEntity)에서 쓸 때 User을 select함
		// eager은 미리 땡겨옴 -> 둘다 select(default)
		// 한 건만 select할 때는 eager, 여러 건을 select하면 lazy(부하 때문)
		return boardsEntity;
	}
}
