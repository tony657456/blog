let index = {
	init: function(){
		$("#btn-save").on("click", ()=>{ // 화살표 함수는 this를 바인딩
			this.save();
		});
		$("#btn-delete").on("click", ()=>{ // 화살표 함수는 this를 바인딩
			this.deleteById();
		});
	},
	
	save: function(){
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		// ajax가 통신을 성공하고 서버가 자동으로 json으로 리턴해줌
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8",
			dataType:"json" // 요청을 서버로해서 응답이 왔을 때 => javascript 오브젝트로 변경
				
		}).done(function(resp){
			alert("글쓰기가 완료되었습니다.")
			console.log(resp)
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error))
		});
	},
	
		deleteById: function(){
			var id = $("#id").text();
			
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			dataType:"json" // 요청을 서버로해서 응답이 왔을 때 => javascript 오브젝트로 변경
		}).done(function(resp){
			alert("삭제가 완료되었습니다.")
			console.log(resp)
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error))
		});
	},
}

index.init();