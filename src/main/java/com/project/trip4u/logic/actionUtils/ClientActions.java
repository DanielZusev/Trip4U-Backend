package com.project.trip4u.logic.actionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.converter.AttributeConverter;
import com.project.trip4u.dao.ElementDao;
import com.project.trip4u.data.ActionType;
import com.project.trip4u.entity.ElementEntity;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.utils.Consts;

@Component
public class ClientActions {

	private static ElementDao elementDao;
	private static AttributeConverter attributeConverter;

	@Autowired
	public void setElementDao(ElementDao elementDao) {
		ClientActions.elementDao = elementDao;
	}

	@Autowired
	public void setAttributeConverter(AttributeConverter attributeConverter) {
		ClientActions.attributeConverter = attributeConverter;
	}

	public static Object actionInvoker(ActionBoundary action) throws ParseException {

		ActionType type = action.getType();
		Map<String, Object> map = new HashMap<>(); // Optional for Get actions

		switch (type) {
		case GENERATE:
			generateTrip(action);

		case DELETE:
			deleteTrip(action);

		case UPDATE:
			UpdateTrip(action);

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

		TripInfo trip = attributeConverter.toAttribute(action.getMoreDetails().get("trip"), TripInfo.class);
		
		int tripDays = getDifferenseBetweenDates(trip.getStartDate(), trip.getEndDate());
		int numOfEvents = (int)Math.ceil(( tripDays * trip.getDayLoad().getValue() ) / trip.getCategories().size());	
		
		for (String category : trip.getCategories()) {
			
			String query = String.format(
					Consts.BASE_URL
					+ "annotate=distance:linestring:%s,%s" 
					+ "&tag_labels=%s" 
					+ "&distance=<10000" 
					+ "&order_by=-score" 
					+ "&count=%d" 
					+ "&fields=name,coordinates,intro,snippet,properties,images,score",
					trip.getStartLocation(), trip.getEndLocation(), category, numOfEvents);
			
		
			
		}

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
