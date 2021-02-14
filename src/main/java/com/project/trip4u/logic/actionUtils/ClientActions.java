package com.project.trip4u.logic.actionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import com.project.trip4u.algorithm.Algorithm;
import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundaryUtils.EventInfo;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.converter.AttributeConverter;
import com.project.trip4u.converter.JsonConverter;
import com.project.trip4u.dao.ElementDao;
import com.project.trip4u.data.ActionType;
import com.project.trip4u.entity.ElementEntity;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.utils.Consts;
import com.project.trip4u.utils.Credentials;

import org.springframework.web.client.RestTemplate;

@Component
public class ClientActions {

	private static ElementDao elementDao;
	private static AttributeConverter attributeConverter;
	private static JsonConverter jsonConverter;

	@Autowired
	public void setElementDao(ElementDao elementDao) {
		ClientActions.elementDao = elementDao;
	}

	@Autowired
	public void setAttributeConverter(AttributeConverter attributeConverter) {
		ClientActions.attributeConverter = attributeConverter;
	}

	@Autowired
	public void setJsonConverter(JsonConverter jsonConverter) {
		ClientActions.jsonConverter = jsonConverter;
	}
	

	public static Object actionInvoker(ActionBoundary action) throws ParseException {

		ActionType type = action.getType();
		Map<String, Object> map = new HashMap<>(); // Optional for Get actions

		switch (type) {
		case GENERATE:
			generateTrip(action);
			break;

		case DELETE:
			deleteTrip(action);
			break;

		case UPDATE:
			UpdateTrip(action);
			break;

		default:
			new NotFoundException("Action Type Not Valid");
		}

		return null;
	}

	private static void UpdateTrip(ActionBoundary action) {
		ElementEntity elementEntity = elementDao.findByElementId(action.getElementId())
				.orElseThrow(() -> new NotFoundException("Element With This Id Not Exist"));

		TripInfo updatedTrip = attributeConverter.toAttribute(action.getMoreDetails().get("trip"), TripInfo.class);
		elementEntity.getMoreDetails().put("trip", updatedTrip);
		elementDao.save(elementEntity);

	}

	private static void deleteTrip(ActionBoundary action) {
		ElementEntity elementEntity = elementDao.findByElementId(action.getElementId())
				.orElseThrow(() -> new NotFoundException("Element With This Id Not Exist"));

		elementDao.deleteById(action.getElementId());

	}

	private static void generateTrip(ActionBoundary action) throws ParseException {

		ArrayList<EventInfo> allEvents = new ArrayList<>();

		TripInfo trip = attributeConverter.toAttribute(action.getMoreDetails().get("trip"), TripInfo.class);

		int tripDays = getDifferenseBetweenDates(trip.getStartDate(), trip.getEndDate());
		int numOfEvents = (int) Math.ceil((tripDays * trip.getDayLoad().getValue()) / trip.getCategories().size());

		for (String category : trip.getCategories()) {
			String query = String.format(
					Consts.BASE_TRIPOSO_URL 
					+ "annotate=distance:linestring:%s,%s" 
					+ "&tag_labels=%s" 
					+ "&distance=<10000"
					+ "&order_by=-score" 
					+ "&count=%d"
					+ "&fields=name,coordinates,intro,snippet,images,properties,score",
					trip.getStartLocation(), trip.getEndLocation(), category, numOfEvents);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(new MediaType("application", "json")));
			headers.add("X-Triposo-Account", Credentials.TRIPOSO_ACCOUNT);
			headers.add("X-Triposo-Token", Credentials.TRIPOSO_TOKEN);

			HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

			ResponseEntity<String> response = restTemplate.exchange(query, HttpMethod.GET, httpEntity, String.class);
			JSONObject events = new JSONObject(response.getBody());
			JSONArray results = events.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				allEvents.add(jsonConverter.toEventInfo(results.getJSONObject(i), category));
			}
		}
		System.out.println(allEvents.size());
		Algorithm.generateTrip(allEvents, trip);
	}

	public static int getDifferenseBetweenDates(String start, String end) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date firstDate = sdf.parse(start);
		Date secondDate = sdf.parse(end);

		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		int days = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		return days;
	}

}
