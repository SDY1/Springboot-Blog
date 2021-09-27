<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<div class="container">
	<form onsubmit="update(event, ${boardEntity.id})">
		<!-- 엔터치려고 이렇게 씀 -->
		<div class="form-group">
			<input id="title" type="text" value="${boardEntity.title }"
				class="form-control" placeholder="Enter title">
		</div>
		<div class="form-group">
			<textarea id="content" class="form-control" row="5">${boardEntity.content }</textarea>
		</div>
		<button type="submit" class="btn btn-primary">수정하기</button>
	</form>
</div>
<script> /* 여기는 나중에 따로 빼기 때문에 el표현식을 넣으면 안됨 */
	async function update(event, id){ // 여기에 ${boardEntity.id} 넣지 않음
		event.preventDefault(); // submit 이벤트가 발생하지 않도록 함(갈때가 없으면 깜박거리는데 이를 막음)
		// 주소: PUT board/3
		// UPDATE board SET title = ?, content = ? WHERE id =?
		let boardUpdateDto  ={
				title: document.querySelector("#title").value,
				content: document.querySelector("#content").value,
		};
//		console.log(boardUpdateDto);
//		console.log(JSON.stringify(boardUpdateDto)); // 통신은 JSON으로 보내야 함
		// JSON.stringify(자바스크립트 오브젝트) => 리턴 json문자열
		// JSON.parse(제이슨 문자열) => 리턴 자바스크립트 함수
		let response = await fetch("http://localhost:8080/board/" + id, { // 패치는 시간이 걸리는 작업이라 await붙여줌
			method: "put",
			body: JSON.stringify(boardUpdateDto),
			headers: {
				"Content-Type": "application/json; charset=utf-8"
			}
		});
		let parseResponse = await response.json(); // 나중에 스프링함수에서 리턴될 때 뭐가 리턴되는지 확인 해보자
		console.log(parseResponse);
		if(parseResponse.code == 1){
			alert("업데이트 성공");
			location.href = "/board/"+id;
		}else{
			alert("업데이트 실패");
		}
	}
	
	$('#content').summernote({
		height : 350
	});
</script>
<%@ include file="../layout/footer.jsp"%>



