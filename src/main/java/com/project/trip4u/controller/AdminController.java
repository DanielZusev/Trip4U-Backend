package com.project.trip4u.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundary.ElementBoundary;
import com.project.trip4u.boundary.UserBoundary;


@RestController
@CrossOrigin(origins = "*")
public class AdminController {
	
	
	@RequestMapping(path = "/admin/users/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("email") String email) {
		
	}
	
	@RequestMapping(path = "/admin/elements/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("email") String email) {
		
	}
	
	@RequestMapping(path = "/admin/actions/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("email") String email) {
	
	}
	
	@RequestMapping(path = "/admin/users/{adminEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllusers(@PathVariable("adminEmail") String email){
		
		return null;
	}
	
	@RequestMapping(path = "/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminEmail") String email){
		
		return null;
	}
	
	@RequestMapping(path = "/admin/elements/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] exportAllElements(@PathVariable("adminEmail") String email){
		
		return null;
	}

}
