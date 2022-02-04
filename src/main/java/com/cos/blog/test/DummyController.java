package com.cos.blog.test;

import java.awt.print.Pageable;
import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class DummyController {
	
	//	@Autowired // 난 이걸 쓰지 않고 생성자 주입 방법을 사용했다.
	private final UserRepository userRepository;
	
	@DeleteMapping("dummy/delete/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		
		return "삭제 되었습니다: " +id;
	}
	
	@Transactional // 함수 종료 시에 자동 commit이 된다. 
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // json 데이터를 요청 => Java Object(MessageConverter의 Jackson 라이브러리로 변환해서 받아줘요
		System.out.println("id: " + id);
		System.out.println("password: " +requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
		@Override
		public IllegalArgumentException get() {
			// TODO Auto-generated method stub
			return new IllegalArgumentException("수정실패");
		}
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
//		userRepository.save(requestUser);
		return null;  
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
//	// 페이징이 안된다ㅠㅠㅠㅠㅠㅠㅠ
//	@GetMapping("/dummy/user")
//	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Direction.DESC) Pageable pageable){
//		List<User> user = userRepository.findAll(pageable);
//		return user;
//	}
	
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// id를 3으로 설정
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user의 4번을 찾으면 내가 데이터베이스에서 못찾아오게 되면 user가 null이 될 것 아냐?
		// 그럼 return null이 리턴이 되잖아.. 그럼 프로그램에 문제가 있지 않겠니?
		// Optional로 너의 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해
		
		// findById를 다음 get()을 쓰면 내 데이터는 절대 null이 나올 수 없어라고 사용하는거라서
		// 좀 위험하고 아래의 방법을 사용한다. 나도 처음 사용해봄...
		// 만약에 프라이머리키가 5번이 없다면 아래의 메서드가 실행된다.
		// 데이터가 없어도 빈 객체가 들어가서 오류가 나지 않는다.
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
		
		// 이 방법은 id에 잘못된 인수가 들어오면 예외를 던져준다.
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
		@Override
		public IllegalArgumentException get() {
			// TODO Auto-generated method stub
			return new IllegalArgumentException("해당 유저는 없습니다. id: " +id);
		}
		});
		// user 객체는 = 자바 오브젝트
		// 변환 (웹브라우저가 이해할 수 있는 데이터) -> json(Gson 라이브러리)
		// 스프링부트 = MessageConverter라는 애가 응답시에 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져줍니다.
		return user;
	}

	// http://localhost:8000/blog/dummy/join
	// http의 body에 usernmae, password, email 데이터를 가지고(요청)
	@PostMapping("/dummy/join")
	public String join(User user) {
		
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate()); 

		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
