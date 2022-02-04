package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Data;
import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDeatils 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.
@Getter
public class PrincipalDetail implements UserDetails{
	private User user; //  콤포지션 

	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되지 않았는지 리턴한다.(true 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠기지 않았는지 리턴
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되지 않았는지 리턴
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화인지 리턴
	@Override
	public boolean isEnabled() {
		return true;
	}

	// 계정이 가지고 있는 권한 목록을 리턴한다.(내가 만든 블로그에서는 권한이 한개라서 루프 필요 x)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		// 자바는 메서드를 node처럼 넘길 수 없다. 그래서 아래 형식을
		// collectors.add(()->{return "ROLE_"+user.getRole()}); 형식으로
		// 사용할 수 있다. 어차피 add 안에 들어갈 수 있는 타입은 GrantedAuthority밖에 없고
		// GrantedAuthority안에 있는 추상메서드(함수)는 getAuthority밖에 없기 때문에 람다식으로
		// 표현이 가능하다.
		collectors.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				// 스프링에서 ROLE을 받을 때 ROLE_ 형태로 받아야 한다.
				return "ROLE_"+user.getRole();
			}
		});
		
		return collectors;
	}
}
