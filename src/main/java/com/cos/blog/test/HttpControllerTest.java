package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

// 사용자 요청 -> 응답(HTML 파일)
// @Controller

// 사용자가 요청 -> 응답(Data)

@RestController
@RequiredArgsConstructor
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest: ";
	
	private final Member m;
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
		System.out.println(TAG+"getter : " + m.getId());
		m.setId(5000);
		System.out.println(TAG+"setter : " + m.getId());
		return "lombok Test 완료";
	}

	// 인터넷 브라우저 요청은 무조건 get요청밖에 할 수가 없다.
	// http://localhost:8000/http/get (select)
	@GetMapping("/http/get")
	public String getTest() {
		return "get 요청";
	}
	
	// http://localhost:8000/http/post (insert)
	@PostMapping("/http/post") // application/json
	public String postTest(@RequestBody Member m) {
		return "post 요청: " + m.getId() + "," + m.getUsername() + "," + m.getPassword();
	}
	
	// http://localhost:8000/http/put (update)
	@PutMapping("/http/put")
	public String putTest() {
		return "put 요청"; 
	}
	
	// http://localhost:8000/http/delete (delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
