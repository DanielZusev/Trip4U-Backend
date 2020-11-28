package com.project.trip4u.entity;



import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActionEntity {
	
	@Id
	private String actionId;
	private String type;
	private String elementId;
	private Date timeStamp;
	private String invokeBy;
	private Map<String, Object> moreDetails;
	
	public ActionEntity() {
		
	}

	public ActionEntity(String id, String type, String elementId, Date timeStamp, String invokeBy,
			Map<String, Object> moreDetails) {
		this.actionId = id;
		this.type = type;
		this.elementId = elementId;
		this.timeStamp = timeStamp;
		this.invokeBy = invokeBy;
		this.moreDetails = moreDetails;
	}

	public String getId() {
		return actionId;
	}

	public void setId(String id) {
		this.actionId = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getInvokeBy() {
		return invokeBy;
	}

	public void setInvokeBy(String invokeBy) {
		this.invokeBy = invokeBy;
	}

	public Map<String, Object> getMoreDetails() {
		return moreDetails;
	}

	public void setMoreDetails(Map<String, Object> moreDetails) {
		this.moreDetails = moreDetails;
	}
	
}
