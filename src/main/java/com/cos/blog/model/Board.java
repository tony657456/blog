package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable =  false, length =  100)
	private String title;
	
	@Lob
	private String content;
	
	private int count; // 조회 수
	
	@OneToMany(mappedBy ="board", fetch = FetchType.EAGER) //  mappedBy 연관관계의 주인이 아니다 (난 FK가 아니에요) DB에 컬럼을 만들지 마세요
	private List<Reply> reply;
	
	@ManyToOne(fetch = FetchType.EAGER) // Many = Board, User = One 한명의 유저는 여러개의 댓글을 작성할 수 있다.
	@JoinColumn(name="userId")
	private User user; // DB는 오브젝트를 저장할 수 없다. 자바는 오브젝트를 저장할 수 있다.
	
	@CreationTimestamp
	private Timestamp createDate;
}
