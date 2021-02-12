package com.project.trip4u.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.service.ActionService;

@RestController
@CrossOrigin(origins = "*")
public class ActionController {
	
	private ActionService actionService;
	
	@Autowired
	public ActionController(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@RequestMapping(path = "/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public Object invokeAction(@RequestBody ActionBoundary actionBoundary) throws ParseException {
		return actionService.invokeAction(actionBoundary);
	}
		
}
