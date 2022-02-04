package com.cos.blog.test;

import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Service
public class Member {

	// private를 사용하는 이유는 객체지향에서는 상태를 행위를 통해서 표현해야하기 때문이다.
	// 어떤 변수값이 50인데 그 값이 그냥 갑자기 100이 되는게 아니라 덧셈이라는 메서드를 통해서 
	// 숫자 100이 되야 한다는 의미이다.
	private int id;
	private String username;
	private String password;
	private String email;
}
