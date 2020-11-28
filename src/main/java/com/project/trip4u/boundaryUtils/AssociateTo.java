package com.project.trip4u.boundaryUtils;

import java.util.Date;

public class AssociateTo {
	
	private String tripId;
	private Date date; //The date that this event appears on the trip
	private Time startHour;
	private Time endHour;
	
	public AssociateTo() {
		
	}

	public AssociateTo(String tripId, Date date, Time startHour, Time endHour) {
		this.tripId = tripId;
		this.date = date;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getStartHour() {
		return startHour;
	}

	public void setStartHour(Time startHour) {
		this.startHour = startHour;
	}

	public Time getEndHour() {
		return endHour;
	}

	public void setEndHour(Time endHour) {
		this.endHour = endHour;
	}
	
}
