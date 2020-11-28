package com.project.trip4u.service;

import java.util.List;

import com.project.trip4u.boundary.ActionBoundary;



public interface ActionService {

	public Object invokeAction(ActionBoundary action);
	
	public List<ActionBoundary> getAllActions(String adminEmail);
	
	public void deleteAllActions(String adminEmail);
}
