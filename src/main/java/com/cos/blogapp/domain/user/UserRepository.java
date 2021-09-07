package com.cos.blogapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// save() 인서트, 업데이트(같은 id넣을 시(primary key))
// findById(1) 셀렉트
// findAll() 전체셀렉트
// deleteById(1) 한건 삭제
// DAO
//@Repository 내부적으로 걸려있음
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value="insert into user (username, password, email) values (:username, :password, :email)",nativeQuery = true)
	void join(String username, String password, String email);

	@Query(value = "select * from user where username = :username and password = :password",nativeQuery =  true)
	User mLogin(String username, String password); // 바인딩 해야해서 오브젝트에 못넣음
	// rs의 결과값을 user오브젝트에 넣어줌
}
