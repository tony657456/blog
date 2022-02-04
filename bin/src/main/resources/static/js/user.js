let index = {
	init: function(){
		$("#btn-save").on("click", ()=>{ // 화살표 함수는 this를 바인딩
			this.save();
		});
	},
	
	save: function(){
		let data = {
			username: $("#username").val(),
			password: $("password").val(),
			email: $("email").val
		};
		
		// ajax가 통신을 성공하고 서버가 자동으로 json으로 리턴해줌
		$.ajax({
			type: "POST",
			url: "/api/user",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8",
			dataType:"json" // 요청을 서버로해서 응답이 왔을 때 => javascript 오브젝트로 변경
				
		}).done(function(resp){
			alert("회원가입이 완료되었습니다.")
			console.log(resp)
			/*location.href = "/";*/
		}).fail(function(error){
			alert(JSON.stringify(error))
		});
	}
}

index.init();