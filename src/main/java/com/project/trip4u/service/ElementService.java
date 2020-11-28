package com.project.trip4u.service;

import java.util.List;

import com.project.trip4u.boundary.ElementBoundary;


public interface ElementService {
	
	public ElementBoundary create(String adminEmail, ElementBoundary element);
	public ElementBoundary update(String adminEmail,String elementId,ElementBoundary update);
	public List<ElementBoundary> getAllElements(String adminEmail);
	public ElementBoundary getSpecificElement(String userEmail, String elementId);
	public void deleteAllElements(String adminEmail);
	
	public void bindExistingElementToAnExistingChildElement(String adminEmail,String originElementId,String childElementId);

	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userEmail,String originElementId);
	public ElementBoundary[] getAnArrayWithElementParent(String userEmail,String originElementId);
	public ElementBoundary[] getAllByName(String userEmail, String name);
	public ElementBoundary[] getAllByType(String userEmail, String type);


}
