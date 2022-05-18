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
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user; 
	}
	
	@Transactional
	public void join(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encode.encode(rawPassword);
		System.out.println("해쉬 된 암호: "+encPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
	}
	
	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화 시키고, 영속화 된 User 오브젝트를 수정
		// select를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서!!
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원찾기 실패");
		});
		String rawPassword = user.getPassword();
		String encPassword = encode.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
		// 회원수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 = commit이 자동으로 된다.
	}
	
//	@Transactional(readOnly = true) // 트랜잭션 정합성 유지
//	public User login(User user) {
//		return userRepository.mLogin(user.getUsername(), user.getPassword());
//	}
}
