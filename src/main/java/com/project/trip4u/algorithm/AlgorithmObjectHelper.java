package com.project.trip4u.algorithm;

import java.util.ArrayList;

import com.project.trip4u.boundaryUtils.EventInfo;

public class AlgorithmObjectHelper {
	
	private ArrayList<EventInfo> route;
	private ArrayList<EventInfo> events;
	private double[][] distanceMatrix;
	private double length;
	
	public AlgorithmObjectHelper() {
		
	}
	
	public AlgorithmObjectHelper(ArrayList<EventInfo> route, ArrayList<EventInfo> events, double[][] distanceMatrix, double length) {
		this.route = route;
		this.events = events;
		this.distanceMatrix = distanceMatrix;
		this.length = length;
	}

	public void setRoute(ArrayList<EventInfo> route) {
		this.route = route;
	}

	public void setEvents(ArrayList<EventInfo> events) {
		this.events = events;
	}

	public void setDistanceMatrix(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public ArrayList<EventInfo> getRoute() {
		return route;
	}

	public ArrayList<EventInfo> getEvents() {
		return events;
	}

	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public double getLength() {
		return length;
	}

}
