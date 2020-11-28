package com.project.trip4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.NewUserBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.service.UserService;



@RestController
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	@RequestMapping(path = "/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary newUser (@RequestBody NewUserBoundary newUserBoundary) {//TODO finish method && consider changing newUderBoundary to UserBoundary
		return userService.createUser(
				new UserBoundary(
						UserRole.valueOf(newUserBoundary.getRole()),
						newUserBoundary.getUsername(),
						newUserBoundary.getEmail(),
						newUserBoundary.getPassword()));
	}
	
	@RequestMapping(path = "/users/login/{userEmail}/{password}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary retrieveUserDetails (@PathVariable("password") String password, @PathVariable("userEmail") String userEmail ) { //TODO check about password
		 return userService.login(userEmail,password);
	}
	
	
	@RequestMapping(path = "/users/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public void updateUserDetails (@RequestBody UserBoundary userBoundary, @PathVariable("userEmail") String userEmail) {//TODO finish method

		
	}
}
