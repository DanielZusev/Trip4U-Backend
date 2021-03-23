package com.project.trip4u.boundaryUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.trip4u.data.DayLoad;

public class TripInfo {

	private String startDate;
	private String endDate;
	private List<String> categories;
	private String passengers;
	private DayLoad dayLoad;
	private String startLocation; //UI sends Coordinates
	private String endLocation; //UI sends Coordinates
	private Map<String, ArrayList<EventInfo>> route;
	private int length;
	
	public TripInfo() {

	}

	public TripInfo(String startDate, String endDate, List<String> categories, String passengers,
			DayLoad dayLoad, String startLocation, String endLocation, Map<String, ArrayList<EventInfo>> route, int length) {
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.categories = categories;
		this.passengers = passengers;
		this.dayLoad = dayLoad;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.route = route;
		this.length = length;
	}


	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getPassengers() {
		return passengers;
	}

	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}

	public DayLoad getDayLoad() {
		return dayLoad;
	}

	public void setDayLoad(DayLoad dayLoad) {
		this.dayLoad = dayLoad;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public Map<String, ArrayList<EventInfo>> getRoute() {
		return route;
	}

	public void setRoute(Map<String, ArrayList<EventInfo>> route) {
		this.route = route;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	
	
}
