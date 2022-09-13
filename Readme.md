## 주요 기능


## 시큐리티 인증관리
```java
package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

// 빈 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration // 빈 등록
@EnableWebSecurity // 시큐리티  필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable() // csrf 토큰 비활성화 
			.authorizeRequests()
			.antMatchers("/", "/user/**", "/js/**", "/css/**", "/images/**", "/dummy/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/user/loginForm")
			.loginProcessingUrl("/user/login") // 스프링 시큐리티가 해당 주소로 오는 요청을 가로채서 대신 로그인을 해준다.
			.defaultSuccessUrl("/");
	}
}
```

## 카카오톡 api를 이용해서 나의 서버 정보랑 merge 시키기
```java
	@GetMapping("/user/auth/kakao/callback")
	public String kakaoCallback(String code) {
		
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
		// UUID -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		System.out.println("블로그서버 패스워드: "+cosKey);
		
		User kakaouser = User.builder()
				.username(kakaoprofile.getKakao_account().getEmail()+"_"+kakaoprofile.getId())
				.password(cosKey)
				.email(kakaoprofile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		// 가입자 혹은 비가입자 체크해서 처리
		User originUser = userService.회원찾기(kakaouser.getUsername());
		
		if(originUser.getUsername() == null) {
			System.out.print("기존 회원이 아닙니다...!!");
			userService.join(kakaouser);
		}
		
		// 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaouser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		
		return "redirect:/";
}
	
```

### 시큐리티 설정
```xml
<!-- 시큐리티 태그 라이브러리 -->
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-taglibs</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
	<version>2.6.1</version>
</dependency>
```

### 스프링 ViewResolver 설정과 JPA 데이터베이스 연결
``` xml
server:
  port: 8000
  servlet:
    encoding:
      charset: UTF-8

spring:
  mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp

  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    username: root
    password: korea1234
    url: jdbc:mysql://localhost:3307/blog

  jpa:
    open-in-view: true
    hibernate:
      ddl-auto: none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      use-new-id-generator-mappings: false #jpa의 기본적략을 따라가지 않음
    show-sql: true
    properties:
      hibernate.format_sql: true

```
### jsp 파일에서 시큐리티 태그
```html
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
<sec:authentication property="principal" var="principal"/>
</sec:authorize>
```
