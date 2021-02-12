package com.project.trip4u.service;

import java.text.ParseException;
import java.util.List;

import com.project.trip4u.boundary.ActionBoundary;



public interface ActionService {

	public Object invokeAction(ActionBoundary action) throws ParseException;
	
	public List<ActionBoundary> getAllActions(String adminEmail);
	
	public void deleteAllActions(String adminEmail);
}
