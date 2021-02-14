package com.project.trip4u.boundaryUtils;

import java.util.Map;

import com.project.trip4u.entity.LocationEntity;

public class EventInfo {

	private String id;
	private String name;
	private LocationEntity location;
	private String intro;
	private String snippet;
	private String label;
	private double score;
	private String imageURL;
	private boolean isVisited;
	private Map<String, String> properties;

	public EventInfo() {

	}

	public EventInfo(String id, String name, LocationEntity location, String intro, String snippet, String label,
			double score, String imageURL, boolean isVisited, Map<String, String> properties) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.intro = intro;
		this.snippet = snippet;
		this.label = label;
		this.score = score;
		this.imageURL = imageURL;
		this.isVisited = isVisited;
		this.properties = properties;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "EventInfo [id=" + id + ", name=" + name + ", location=" + location + ", intro=" + intro + ", snippet="
				+ snippet + ", label=" + label + ", score=" + score + ", imageURL=" + imageURL + ", isVisited="
				+ isVisited + ", properties=" + properties + "]";
	}

	
}
