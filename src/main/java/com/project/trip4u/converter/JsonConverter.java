package com.project.trip4u.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.project.trip4u.boundaryUtils.EventInfo;
import com.project.trip4u.entity.LocationEntity;

@Component
public class JsonConverter {

	public EventInfo toEventInfo(JSONObject jsonObject, String category) {
		EventInfo eventInfo = new EventInfo();
		eventInfo.setId(UUID.randomUUID().toString());
		eventInfo.setName(getObjectStr(jsonObject, "name"));
		eventInfo.setSnippet(getObjectStr(jsonObject, "snippet"));
		eventInfo.setIntro(getObjectStr(jsonObject, "intro"));
		eventInfo.setScore(Double.parseDouble(getObjectStr(jsonObject, "score")));
		eventInfo.setLocation(getLocationEntity(jsonObject));
		eventInfo.setImageURL(getObjectStr(jsonObject.getJSONArray("images").getJSONObject(0), "source_url"));
		eventInfo.setLabel(category);
		eventInfo.setProperties(getObjectProperties(jsonObject.getJSONArray("properties")));
		eventInfo.setVisited(false);
		return eventInfo;
	}
	
	public double[][] toDistanceMatrix(JSONArray jsonArray, int numOfEvents) {
		double[][] distanceMatrix = new double[numOfEvents][numOfEvents];
		for(int i = 0; i < numOfEvents; i++) {
			for(int j = 0; j < numOfEvents; j++) {
				distanceMatrix[i][j] = Double.parseDouble(jsonArray.getJSONObject((i * numOfEvents) + j).get("travelDistance").toString());
			}
		}
		return distanceMatrix;
	}

	private String getObjectStr(JSONObject jsonObject, String objectName) {
		try {
			String object = jsonObject.get(objectName).toString();
			return object;
		} catch (Exception e) {
			return null;
		}
	}

	private LocationEntity getLocationEntity(JSONObject jsonObject) {
		try {
			LocationEntity locationEntity = new LocationEntity();
			locationEntity.setLat(jsonObject.getJSONObject("coordinates").getDouble("latitude"));
			locationEntity.setLng(jsonObject.getJSONObject("coordinates").getDouble("longitude"));
			return locationEntity;
		} catch (Exception e) {
			return new LocationEntity(0.0, 0.0);
		}
	}

	private Map<String, String> getObjectProperties(JSONArray jsonArray) {
		if(jsonArray.length() != 0) {
			Map<String, String> map = new HashMap<>();
			for(int i = 0; i < jsonArray.length(); i++) {
				String key = jsonArray.getJSONObject(i).get("name").toString();
				String value = jsonArray.getJSONObject(i).get("value").toString();
				map.put(key, value);
			}
		}
		return new HashMap<>();
	}

}
