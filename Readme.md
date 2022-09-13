# 주요 기능

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
