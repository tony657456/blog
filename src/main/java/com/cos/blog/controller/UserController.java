package com.cos.blog.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.Kakaoprofile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /user/**허용
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js, /css, /images
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/user/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@GetMapping("/user/loginForm")
	public String loginForm() {
		return "user/loginForm"; 
	}
	
	@GetMapping("/user/auth/kakao/callback")
	public @ResponseBody String kakaoCallback(String code) {
		
		// POST방식으로 key=value 데이터를 요청(카카오쪽으로)
		// Retrofit2
		// OkHttp
		// RestTemplate
		RestTemplate rt = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", 	"0c4ee53180ec608c6e170694ece92fd2");
		params.add("redirect_uri", "http://localhost:8000/user/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);
		
		// Gson, JsonSimple, ObjectMapper 3개의 json 데이터를 오브젝트로
		// 변경해주는 라이브러리
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oAuthToken = null;
		try {
			oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰: " + oAuthToken.getAccess_token());
		
		// Spring 3.0부터 지원하고 스프링에서 제공하는 http 통신을 유용하게 쓸 수 있는 템플릿이다.
		// http 서버와의 통신을 단순환 하고 RESTful 원칙을 지켜 json과 xml을 쉽게 응답 받는다.
		RestTemplate rt2 = new RestTemplate();
		
		// 카카오톡에 필요한 정보들을 카카오톡 형식에 맞게 그대로 담아서 보내준다.
		HttpHeaders headers2= new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기(body의 정보는 없으니까 담지 않는다)
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);
		
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
				);
		
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		Kakaoprofile kakaoprofile = null;
		try {
			kakaoprofile = objectMapper2.readValue(response2.getBody(), Kakaoprofile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// User 오브젝트: username, password, email
		System.out.println("카카오 아이디(번호): "+kakaoprofile.getId());
		System.out.println("카카오 이메일(번호): "+kakaoprofile.getKakao_account().getEmail());
		
		System.out.println("블로그서버 유저네임: "+kakaoprofile.getKakao_account().getEmail()+"_"+kakaoprofile.getId());
		System.out.println("블로그서버 이메일: "+kakaoprofile.getKakao_account().getEmail());
		UUID garbagePassword = UUID.randomUUID();
		System.out.println("블로그서버 패스워드: "+garbagePassword);
		
		User kakaouser = User.builder()
				.username(kakaoprofile.getKakao_account().getEmail()+"_"+kakaoprofile.getId())
				.password(garbagePassword.toString())
				.email(kakaoprofile.getKakao_account().getEmail())
				.build();
		
		// 가입자 혹은 비가입자 체크해서 처리
		User originUser = userService.회원찾기(kakaouser.getUsername());
		
		if(originUser.getUsername() == null) {
			System.out.print("기존 회원이 아닙니다...!!");
			userService.join(kakaouser);
		}
		
		// 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaouser.getUsername(), kakaouser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		
		return "redirect:/";
}
	
	@GetMapping("/userinformation")
	public String userinformationForm(@AuthenticationPrincipal Principal principal) {
		return "user/userinformationForm";
	}
}
