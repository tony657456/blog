package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cos.blog.model.User;

// DAO
// JPA는 자동으로 bean으로 등록인 된다.
// @Repository가 생략이 가능하다.
// User 테이블의 프라이머리키는 Integer 숫자야라고 지정
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// SELECT * FROM user WHERE username=1?;
	Optional<User> findByUsername(String username);
	
	
//	@Query(value="SELECT * FROM user WHERE username = :username AND password = :password", nativeQuery = true)
//	User mLogin(@Param("username") String username, @Param("password") String password);
	
}
