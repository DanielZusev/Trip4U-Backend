package com.project.trip4u.boundary;

import java.util.Date;
import java.util.Map;

import com.project.trip4u.data.ActionType;





public class ActionBoundary {
	
	private String actionId;
	private ActionType type;
	private String elementId;
	private Date timeStamp;
	private String invokeBy;
	private Map<String, Object> moreDetails;
	
	public ActionBoundary() {
		
	}


	public ActionBoundary(String id, ActionType type, String element, Date timeStamp, String invokeBy,
						  Map<String, Object> moreDetails) {
		this.actionId = id;
		this.type = type;
		this.elementId = element;
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


	public ActionType getType() {
		return type;
	}


	public void setType(ActionType type) {
		this.type = type;
	}


	public String getElementId() {
		return elementId;
	}


	public void setElementId(String element) {
		this.elementId = element;
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
