<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>

<div class="container">

	<button class="btn btn-secondary" onclick="history.back()">돌아가기</button>
	<button id="btn-update" class="btn btn-warning">수정</button>
	<c:if test="${board.user.id == principal.user.id}">
		<button id="btn-delete" class="btn btn-danger">삭제</button>
	</c:if>
	<br /> <br />
	<div>
		글 번호: <span id="id"><i>${board.id} </i></span> 작성자: <span><i>${board.user.username} </i></span> <br />
	</div>
	<div class="form-group">
		<h3>${board.title}</h3>
	</div>
	<hr />
	<div class="form-group">
		<div>${board.content}</div>
	</div>
	<hr />
	<button id="btn-save" class="btn btn-primary">글쓰기 완료</button>
</div>

<script src="/js/board.js"></script>
<%@include file="../layout/footer.jsp"%>



