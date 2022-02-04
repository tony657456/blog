package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

	// 브라우저가 인식할 수 있는 파일은 정적 파일이다. 그래서 static 폴더 아래에 파일을 만들게 되면
	// html, Img 파일을 인식할 수 있지만 .jsp 파일은 동적인 파일이기 때문에 경로를 지정해주어야 한다. 
	@GetMapping("/temp/home")
	public String tempHome() {
		// 파일 리턴 기본경로: src/main/resource/static
		// 리턴명: /home.html
		// 풀경로: src/main/resource/static/home.html
		return "/home.html"; 
	}
	
	@GetMapping("/temp/img")
	public String tempImg() {
		return "/a.png";
	}
	
	@GetMapping("/test/jsp")
	public String testjsp() {
		return "test";
	}
}
