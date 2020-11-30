package com.project.trip4u.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.ActionBoundary;

@RestController
@CrossOrigin(origins = "*")
public class ActionController {
	
	@RequestMapping(path = "/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public Object invokeAction(@RequestBody ActionBoundary actionBoundary) {
		return null;
	}
		
}
