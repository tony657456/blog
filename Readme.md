
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

```html
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
<sec:authentication property="principal" var="principal"/>
</sec:authorize>
```
