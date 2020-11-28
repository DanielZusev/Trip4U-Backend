package com.project.trip4u.boundaryUtils;

import java.util.Date;
import java.util.List;

import com.project.trip4u.data.DayLoad;

public class TripInfo {
	
	private String user;
	private Date startDate;
	private Date endDate;
	private List<String> categories;
	private String passengers;
	private DayLoad dayLoad;
	private String destination;
	
	public TripInfo() {

	}

	public TripInfo(String user, Date startDate, Date endDate, List<String> categories, String passengers,
			DayLoad dayLoad, String destination) {
		this.user = user;
		this.startDate = startDate;
		this.endDate = endDate;
		this.categories = categories;
		this.passengers = passengers;
		this.dayLoad = dayLoad;
		this.destination = destination;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
}
