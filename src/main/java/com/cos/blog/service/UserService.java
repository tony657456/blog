package com.cos.blog.service;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encode;
	
	@Transactional
	public void join(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encode.encode(rawPassword);
		System.out.println("해쉬 된 암호: "+encPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
	}
	
//	@Transactional(readOnly = true) // 트랜잭션 정합성 유지
//	public User login(User user) {
//		return userRepository.mLogin(user.getUsername(), user.getPassword());
//	}
}
