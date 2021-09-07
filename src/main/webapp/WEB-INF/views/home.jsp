<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp"%>

<h1>${sessionScope.principal.username }</h1>
<!-- 찾을 때 request랑 session이 충돌이 나면 범위가 작은거로 선택됨. 따라서 구분하는게 좋음(sessionScope.)-->

<%@ include file="layout/footer.jsp"%>
    