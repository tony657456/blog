<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../layout/header.jsp"%>

<div class="container">
	<form action="/user/login" method="post">
		<div class="form-group">
			<label for="username">Username</label> <input type="text" name="username" class="form-control" placeholder="Enter Username" id="username">
		</div>

		<div class="form-group">
			<label for="email">Password</label> <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		<button id="btn-login" class="btn btn-primary">로그인</button>
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=0c4ee53180ec608c6e170694ece92fd2&redirect_uri=http://localhost:8000/user/auth/kakao/callback&response_type=code"><img height="38px" src="/images/kakao_login_medium.png"/></a>
	</form>

</div>
<script src="/js/user.js"></script>
<%@include file="../layout/footer.jsp"%>