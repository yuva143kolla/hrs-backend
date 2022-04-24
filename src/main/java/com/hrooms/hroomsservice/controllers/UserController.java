package com.hrooms.hroomsservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrooms.hroomsservice.entities.User;
import com.hrooms.hroomsservice.modal.AuthReqBody;
import com.hrooms.hroomsservice.modal.AuthResBody;
import com.hrooms.hroomsservice.repository.UserRepository;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	UserRepository userRepo;

	@PostMapping("/login")
	public AuthResBody authenticate(@RequestBody AuthReqBody authReqBody) {
		AuthResBody auth = new AuthResBody();
		auth.setAuthenticated(false);
		List<User> users = userRepo.getUserByNameAndPwd(authReqBody.getUsername(), authReqBody.getPassword());
		if (users != null && !users.isEmpty()) {
			auth.setUser(users.get(0));
			auth.setAuthenticated(true);
		}

		return auth;
	}
}
