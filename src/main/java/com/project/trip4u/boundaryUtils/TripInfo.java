package com.project.trip4u.boundaryUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.trip4u.data.DayLoad;

public class TripInfo {

	private Date startDate;
	private Date endDate;
	private List<String> categories;
	private String passengers;
	private DayLoad dayLoad;
	private String startLocation;
	private String endLocation;
	private ArrayList<EventInfo> route;
	
	public TripInfo() {

	}

	public TripInfo(Date startDate, Date endDate, List<String> categories, String passengers,
			DayLoad dayLoad, String startLocation, String endLocation, ArrayList<EventInfo> route) {
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.categories = categories;
		this.passengers = passengers;
		this.dayLoad = dayLoad;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.route = route;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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

	public ArrayList<EventInfo> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<EventInfo> route) {
		this.route = route;
	}

	
	
}
