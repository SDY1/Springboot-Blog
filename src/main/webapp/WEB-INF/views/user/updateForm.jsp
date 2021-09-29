<!-- 자바파일과 달리 jsp파일은 수정하고 리로드한다고 수정 안됨 -> 사용자가 리퀘스트 할 때 읽기 때문
-> 웹에서 새로고침해야 수정됨 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>
<!-- container 가운데 정렬 전체화면의 80% -->
<div class="container">
	<form onsubmit="update(event, ${sessionScope.principal.id })">
		<div class="form-group">
			<input type="text" value="${sessionScope.principal.username }"
				class="form-control" placeholder="Enter username"
				required="required" maxlength="20" readonly>
		</div>
		<div class="form-group">
			<input type="email"  id="email" value="${sessionScope.principal.email }"
				class="form-control" placeholder="Enter email" required="required">
		</div>
		<button type="submit" class="btn btn-primary">회원수정</button>
	</form>
</div>

<script>
	async function update(event, id){ 
		event.preventDefault(); 
		// 주소: PUT board/3
		// UPDATE board SET title = ?, content = ? WHERE id =?
		let userUpdateDto  ={
				email: document.querySelector("#email").value,
		};
		let response = await fetch("http://localhost:8080/user/" + id, { 
			method: "put",
			body: JSON.stringify(userUpdateDto),
			headers: {
				"Content-Type": "application/json; charset=utf-8"
			}
		});
		let parseResponse = await response.json(); 
		console.log(parseResponse);
		if(parseResponse.code == 1){
			alert("업데이트 성공");
			location.href = "/";
		}else{
			alert("업데이트 실패 : "+parseResponse.msg);
		}
	}
</script>
<%@ include file="../layout/footer.jsp"%>


