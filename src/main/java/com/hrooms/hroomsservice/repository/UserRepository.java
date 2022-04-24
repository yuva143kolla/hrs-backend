package com.hrooms.hroomsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hrooms.hroomsservice.entities.User; 

@Repository
public interface UserRepository extends JpaRepository<User, Long>{ 
	
	@Query(nativeQuery = true, value ="select * from users where loginId= :userName and password= :password")
	public List<User> getUserByNameAndPwd(String userName, String password);
}
