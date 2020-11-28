package com.project.trip4u.boundary;
import java.util.Map;

public class ElementBoundary {
	
	private String elementId;
	private String type;
	private String name;
	private Map<String,Object> moreDetails; //trip date, trip created by, trip locations, event location
	
	
	public ElementBoundary() {
		
	}


	public ElementBoundary(String elementId, String type, String name, Map<String, Object> moreDetails) {
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.moreDetails = moreDetails;
	}


	public String getElementId() {
		return elementId;
	}


	public void setElementId(String elementId) {
		this.elementId = elementId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Map<String, Object> getMoreDetails() {
		return moreDetails;
	}


	public void setMoreDetails(Map<String, Object> moreDetails) {
		this.moreDetails = moreDetails;
	}
	
	
}
