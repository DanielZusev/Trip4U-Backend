package com.project.trip4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundary.ElementBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.service.UserService;


@RestController
@CrossOrigin(origins = "*")
public class AdminController {
	
private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	@RequestMapping(path = "/admin/users/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("adminEmail") String adminEmail) {
		this.userService.deleteAllUsers(adminEmail);
	}
	
	@RequestMapping(path = "/admin/elements/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("adminEmail") String adminEmail) {
		
	}
	
	@RequestMapping(path = "/admin/actions/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("adminEmail") String adminEmail) {
	
	}
	
	@RequestMapping(path = "/admin/users/{adminEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllusers(@PathVariable("adminEmail") String adminEmail){
		
		return userService.getAllUsers(adminEmail).toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(path = "/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminEmail") String adminEmail){
		
		return null;
	}
	
	@RequestMapping(path = "/admin/elements/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] exportAllElements(@PathVariable("adminEmail") String adminEmail){
		
		return null;
	}

}
