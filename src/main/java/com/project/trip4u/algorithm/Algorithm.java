package com.project.trip4u.algorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	public static TripInfo generateTrip(ArrayList<EventInfo> events, TripInfo tripInfo){
		AlgorithmObjectHelper algorithmObjectHelper = new AlgorithmObjectHelper();
		double totalLength = 0;
		ArrayList<EventInfo> totalRoute = new ArrayList<>();
		Map<String, ArrayList<EventInfo>> routeByDays = new HashMap<>();
		// Calculate distance matrix
		double[][] distanceMatrix = calculateDistanceMatrix(events);

		algorithmObjectHelper.setDistanceMatrix(distanceMatrix);
	
		for(int i = 0; i < events.size(); i++) {
			double length = 0;
			ArrayList<EventInfo> eventsCopy = new ArrayList<>(); 
			eventsCopy.addAll(events);
			for(int j = 0; j < eventsCopy.size(); j++) {
				eventsCopy.get(j).setVisited(false);
			}
			ArrayList<EventInfo> route = new ArrayList<>();
			// what to do with start point????????????????
			route.add(eventsCopy.get(i));
			eventsCopy.get(i).setVisited(true);
			algorithmObjectHelper.setEvents(eventsCopy);
			algorithmObjectHelper.setRoute(route);
			algorithmObjectHelper.setLength(length);

			while(!checkAllVisited(algorithmObjectHelper.getEvents())) {
				algorithmObjectHelper = findNearestEvent(algorithmObjectHelper);
			}
			if(totalLength == 0) { // first route
				totalLength = algorithmObjectHelper.getLength();
				totalRoute.addAll(algorithmObjectHelper.getRoute());
			} else {
				if(algorithmObjectHelper.getLength() < totalLength) {
					totalLength = algorithmObjectHelper.getLength();
					totalRoute.clear();
					totalRoute.addAll(algorithmObjectHelper.getRoute());
				}
			}
			
		}
		for(int i = 0; i < tripInfo.getLength(); i++) {
			ArrayList<EventInfo> dayRoute = new ArrayList<>();
			for(int j = 0; j < tripInfo.getDayLoad().getValue(); j++) {
				dayRoute.add(totalRoute.get((i * tripInfo.getDayLoad().getValue()) + j));
			}
			routeByDays.put(String.valueOf(i + 1), dayRoute);
		}
		//tripInfo.setRoute(totalRoute);
		tripInfo.setRoute(routeByDays);
		return tripInfo;
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
	private static AlgorithmObjectHelper findNearestEvent(AlgorithmObjectHelper algorithmObjectHelper) {
		double minDistance = 0;
		int tempIndex = -1;
		ArrayList<EventInfo> events = new ArrayList<>();
		ArrayList<EventInfo> route = new ArrayList<>();
		double[][] distanceMatrix;
		double length;
		events.addAll(algorithmObjectHelper.getEvents());
		route.addAll(algorithmObjectHelper.getRoute());
		distanceMatrix = algorithmObjectHelper.getDistanceMatrix();
		length = algorithmObjectHelper.getLength();
		try {
			int eventIndex = findEventIndex(events, route.get(route.size() - 1));
			for(int k = 0; k < events.size(); k++) {
				if(!events.get(k).isVisited()) {
					if(minDistance == 0) { // First round
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
				events.get(tempIndex).setVisited(true);
				length += minDistance;
				return new AlgorithmObjectHelper(route, events, distanceMatrix, length);
			}
			return new AlgorithmObjectHelper();
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
