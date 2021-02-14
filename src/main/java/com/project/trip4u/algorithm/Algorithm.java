package com.project.trip4u.algorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.project.trip4u.boundaryUtils.EventInfo;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.converter.AttributeConverter;
import com.project.trip4u.converter.JsonConverter;
import com.project.trip4u.entity.ElementEntity;
import com.project.trip4u.logic.actionUtils.ClientActions;
import com.project.trip4u.utils.Consts;
import com.project.trip4u.utils.Credentials;

@Component
public class Algorithm {
	
	private static AttributeConverter attributeConverter;
	private static JsonConverter jsonConverter;
	
	@Autowired
	public void setAttributeConverter(AttributeConverter attributeConverter) {
		Algorithm.attributeConverter = attributeConverter;
	}
	
	@Autowired
	public void setJsonConverter(JsonConverter jsonConverter) {
		Algorithm.jsonConverter = jsonConverter;
	}
	
	// Algorithm
	public static ArrayList<EventInfo> generateTrip(ArrayList<EventInfo> events, TripInfo tripInfo){
		double totalLength = 0;
		ArrayList<EventInfo> totalRoute = new ArrayList<>();
		// Calculate distance matrix
		double[][] distanceMatrix = calculateDistanceMatrix(events);
		for(int i = 0; i < distanceMatrix.length; i++) {
			for(int j = 0; j < distanceMatrix[i].length; j++) {
				System.out.print(distanceMatrix[i][j] + "\t\t");
			}
			System.out.println();
		}
//		for(int i = 0; i < events.size(); i++) {
//			double length = 0;
//			ArrayList<EventInfo> eventsCopy = events; // BEWARE!!!!!!!!!!
//			ArrayList<EventInfo> route = new ArrayList<>();
//			// what to do with start point????????????????
//			route.add(eventsCopy.get(i));
//			//length += distanceMatrix[i][i];
//			eventsCopy.get(i).setVisited(true);
//			while(!checkAllVisited(eventsCopy)) {
//				findNearestEvent(route, eventsCopy, distanceMatrix, length);
//			}
//			if(totalLength == 0) { // first route
//				totalLength = length;
//				totalRoute = route;
//			} else {
//				if(length < totalLength) {
//					totalLength = length;
//					totalRoute = route;
//				}
//			}
//			
//		}
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
	private static double[][] calculateDistanceMatrix(ArrayList<EventInfo> events){
		String points = "";
		for(int i = 0; i < events.size(); i++) {
			points += events.get(i).getLocation().toString();
			if(i != events.size() - 1) {
				points += ";";
			}
		}
		System.out.println(points);
		String query = String.format(
				Consts.BASE_BING_URL 
				+ "origins=%s" 
				+ "&destinations=%s" 
				+ "&travelMode=driving" 
				+ "&distanceUnit=km" 
				+ "&key=" 
				+ Credentials.BING_KEY,
				points, points);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(new MediaType("application", "json")));

		HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		ResponseEntity<String> response = restTemplate.exchange(query, HttpMethod.GET, httpEntity, String.class);
		JSONObject distanceMatrixResponse = new JSONObject(response.getBody());
		JSONArray results = distanceMatrixResponse.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources").getJSONObject(0).getJSONArray("results");
		return jsonConverter.toDistanceMatrix(results, events.size());
	}
	
	// Function that finds the nearest event.
	private static void findNearestEvent(ArrayList<EventInfo> route, ArrayList<EventInfo> events, double[][] distanceMatrix, double length) {
		double minDistance = 0;
		int tempIndex = -1;
		try {
			int eventIndex = findEventIndex(events, route.get(route.size() - 1));
			for(int k = 0; k < events.size(); k++) {
				if(!events.get(k).isVisited()) {
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
				events.get(tempIndex).setVisited(true);;
				length += minDistance;
			}
		} catch(Exception e) {
			throw new IndexOutOfBoundsException("Index out of bounds.");
		}
	}
	
	// Function that checks if all events are visited.
	private static boolean checkAllVisited(ArrayList<EventInfo> events) {
		for(int i = 0; i < events.size(); i++) {
			if(!events.get(i).isVisited())
				return false;
		}
		return true;
	}
	
	// Function that finds the events index of specific event in the events array.
	private static int findEventIndex(ArrayList<EventInfo> events, EventInfo event) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getId().equals(event.getId())) {
				return i;
			}
		}
		return -1;
	}
}
