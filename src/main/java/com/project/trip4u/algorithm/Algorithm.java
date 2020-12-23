package com.project.trip4u.algorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.trip4u.boundaryUtils.EventInfo;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.converter.AttributeConverter;
import com.project.trip4u.entity.ElementEntity;

@Component
public class Algorithm {
	
	private static AttributeConverter attributeConverter;
	
	@Autowired
	public void setAttributeConverter(AttributeConverter attributeConverter) {
		Algorithm.attributeConverter = attributeConverter;
	}
	
	// Algorithm
	private static ArrayList<ElementEntity> generateTrip(ArrayList<ElementEntity> events, TripInfo tripInfo){
		double totalLength = 0;
		ArrayList<ElementEntity> totalRoute = new ArrayList<>();
		// Calculate distance matrix
		double[][] distanceMatrix = calculateDistanceMatrix(events);
		for(int i = 0; i < events.size(); i++) {
			double length = 0;
			ArrayList<ElementEntity> eventsCopy = events; // BEWARE!!!!!!!!!!
			ArrayList<ElementEntity> route = new ArrayList<>();
			// what to do with start point????????????????
			route.add(eventsCopy.get(i));
			//length += distanceMatrix[i][i];
			setVisited(eventsCopy.get(i));
			while(!checkAllVisited(eventsCopy)) {
				findNearestEvent(route, eventsCopy, distanceMatrix, length);
			}
			if(totalLength == 0) {
				totalLength = length;
				totalRoute = route;
			} else {
				if(length < totalLength) {
					totalLength = length;
					totalRoute = route;
				}
			}
			
		}
		return totalRoute;
	}
	
	// Function that calculates the difference between the days.
	private long calculateDayDifference(Date date1, Date date2) {
		LocalDate startDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long days = ChronoUnit.DAYS.between(startDate, endDate);
		return days;
	}
	
	// Function that calculates the distance matrix.
	private static double[][] calculateDistanceMatrix(ArrayList<ElementEntity> events){
		
		return null;
	}
	
	// Function that finds the nearest event.
	private static void findNearestEvent(ArrayList<ElementEntity> route, ArrayList<ElementEntity> events, double[][] distanceMatrix, double length) {
		double minDistance = 0;
		int tempIndex = -1;
		try {
			int eventIndex = findEventIndex(events, route.get(route.size() - 1));
			for(int k = 0; k < events.size(); k++) {
				if(!checkVisited(events.get(k))) {
					if(minDistance == 0) {
						minDistance = distanceMatrix[eventIndex][k];
						tempIndex = k;
					} else {
						if(distanceMatrix[eventIndex][k] < minDistance) {
							minDistance = distanceMatrix[eventIndex][k];
							tempIndex = k;
						}
					}
				}
			}
			if(tempIndex != -1) {
				route.add(events.get(tempIndex));
				setVisited(events.get(tempIndex));
				length += minDistance;
			}
		} catch(Exception e) {
			throw new IndexOutOfBoundsException("Index out of bounds.");
		}
	}
	
	// Function that checks if event is visited.
	private static boolean checkVisited(ElementEntity event) {
		EventInfo eventInfo = attributeConverter.toAttribute(event.getMoreDetails().get("EventInfo"), EventInfo.class);
		return eventInfo.isVisited();
	}
	
	// Function that sets visited to true;
	private static void setVisited(ElementEntity event) {
		EventInfo eventInfo = attributeConverter.toAttribute(event.getMoreDetails().get("EventInfo"), EventInfo.class);
		eventInfo.setVisited(true);
		event.getMoreDetails().put("EventInfo", eventInfo);
	}
	
	// Function that checks if all events are visited.
	private static boolean checkAllVisited(ArrayList<ElementEntity> events) {
		for(int i = 0; i < events.size(); i++) {
			if(!checkVisited(events.get(i)))
				return false;
		}
		return true;
	}
	
	// Function that finds the events index.
	private static int findEventIndex(ArrayList<ElementEntity> events, ElementEntity event) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getElementId().equals(event.getElementId())) {
				return i;
			}
		}
		return -1;
	}
}
