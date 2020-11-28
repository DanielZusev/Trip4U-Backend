package com.project.trip4u.entity;

import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ElementEntity {
	
	@Id
	private String elementId;
	private String type;
	private String name;
	private Map<String,Object> moreDetails;
	private Set<ElementEntity> childElements;
	
	public ElementEntity() {
		
	}

	public ElementEntity(String elementId, String type, String name, Map<String, Object> moreDetails,
			Set<ElementEntity> childElements) {
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.moreDetails = moreDetails;
		this.childElements = childElements;
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

	public Set<ElementEntity> getChildElements() {
		return childElements;
	}

	public void setChildElements(Set<ElementEntity> childElements) {
		this.childElements = childElements;
	}
	
}
