package com.project.trip4u.boundaryUtils;

import com.project.trip4u.entity.LocationEntity;

public class EventInfo {
	
	private LocationEntity location;
	private String description;
	private String catagory;
	private Price price;
	private int score;
	private String imageURL;
	private AssociateTo association;
	private boolean isVisited;
	
	public EventInfo() {

	}

	public EventInfo(LocationEntity location, String description, String catagory, Price price, int score,
			String imageURL, AssociateTo association) {
		super();
		this.location = location;
		this.description = description;
		this.catagory = catagory;
		this.price = price;
		this.score = score;
		this.imageURL = imageURL;
		this.association = association;
		this.isVisited = false;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public AssociateTo getAssociation() {
		return association;
	}

	public void setAssociation(AssociateTo association) {
		this.association = association;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	
	
}
