package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Entity // User 클래스가 MySQL에 자동으로 테이블이 생성
//@Builder // Builder를 사용하면 생성자가 기본으로 다 생성이 된다. 난 이거 사용 x
// @DynamicInsert // insert시에 null인 필드를 제외시켜 준다.
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 어떤 데이터 베이스를 쓰던 그 전략을 따라간다.
	private int id;
	
	@Column(nullable = false, unique = true, length = 100)
	private String username;
	
	@Column(nullable = false, length = 100) // 해쉬로 비밀번호 암호화
	private String password;
	
	@Column(nullable = false, length = 100)
	private String email;
	
	// @ColimnDefault 어노테이션 값을 넣음으로 인해서 쿼리 value에는 null값이 들어간다.

	//@ColumnDefault("user")
	// db는 RoleType이라는게 없다.
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는게 좋다. // admin, user, manager
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}
