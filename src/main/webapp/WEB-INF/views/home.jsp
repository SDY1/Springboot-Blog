<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp"%>

<!-- 찾을 때 request랑 session이 충돌이 나면 범위가 작은거로 선택됨. 따라서 구분하는게 좋음(sessionScope.)-->
<h1>${sessionScope.principal.username }</h1>

<div class="container">
	<c:forEach begin="0" end="2">
		<!-- 카드 글 시작 -->
		<div class="card">
			<div class="card-body">
				<h4 class="card-title">Title 부분입니다</h4>
				<a href="#" class="btn btn-primary">상세보기</a>
			</div>
		</div>
		<br />
		<!-- 카드 글 끝 -->
	</c:forEach>
		<ul class="pagination d-flex justify-content-center">
			<li class="page-item disabled"><a class="page-link" href="#">Prev</a></li>
			<li class="page-item"><a class="page-link" href="#">Next</a></li>
		</ul>
</div>
<%@ include file="layout/footer.jsp"%>
