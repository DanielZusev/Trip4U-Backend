package com.project.trip4u;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundary.NewUserBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.boundaryUtils.UserName;
import com.project.trip4u.data.ActionType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TripTests {
	
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String adminEmail;
	private String touristEmail;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/trips";
		this.adminEmail = "admin@gmail.com";
		this.touristEmail = "tour@gmail.com";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void addAdminToDB() {
		NewUserBoundary input = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
		NewUserBoundary tourist = new NewUserBoundary("TOURIST", new UserName("Tourist", "Test"), touristEmail,"123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", tourist, UserBoundary.class);
	}
	
	@AfterEach
	public void teardown() { // Admin URL : Delete All Users, action and trips
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/actions/{adminEmail}", this.adminEmail);
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/trips/{adminEmail}", this.adminEmail);
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}
	
	@Test
	public void testPostTripAndRetriveItFromDB() {
		// GIVEN the server is up
		// POST /actions
		String[] categories = { "culture", "hiking" };
		Map<String, Object> trip = new HashMap<String, Object>() {
			{
				put("startDate", "12/17/2020");
				put("endDate", "12/19/2020");
				put("categories", categories);
				put("dayLoad", "CALM");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		Map<String, Object> moreDetails = new HashMap<String, Object>() {
			{
				put("trip", trip);
			}
		};
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
				
		TripInfo tripI = this.restTemplate.getForObject(url + "/{userEmail}/{tripId}", TripInfo.class, this.adminEmail, tripInfo.get("tripId").toString());
		assertThat(tripI).isNotNull();
	}
	
	@Test
	public void testPost2TripsAndRetriveItFromDB() {
		// GIVEN the server is up
		// POST /actions
		String[] categories = { "culture", "hiking" };
		Map<String, Object> trip = new HashMap<String, Object>() {
			{
				put("startDate", "12/17/2020");
				put("endDate", "12/19/2020");
				put("categories", categories);
				put("dayLoad", "CALM");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		Map<String, Object> moreDetails = new HashMap<String, Object>() {
			{
				put("trip", trip);
			}
		};
		Map<String, Object> trip1 = new HashMap<String, Object>() {
			{
				put("startDate", "12/16/2020");
				put("endDate", "12/21/2020");
				put("categories", categories);
				put("dayLoad", "INTENSE");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		Map<String, Object> moreDetails1 = new HashMap<String, Object>() {
			{
				put("trip", trip1);
			}
		};
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.touristEmail, moreDetails);
		ActionBoundary action1 = new ActionBoundary(null, ActionType.GENERATE, null, null, this.touristEmail, moreDetails1);
		
		this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action1, HashMap.class);
		
		TripInfo[] trips = this.restTemplate.getForObject(url + "/{userEmail}", TripInfo[].class, this.touristEmail);
		assertThat(trips).hasSize(2);
	}
	
	@Test
	public void testGenerateTripAndCheckCategories() throws JSONException {
		String[] categories = { "culture", "hiking" };
		Map<String, Object> trip = new HashMap<String, Object>() {
			{
				put("startDate", "12/17/2020");
				put("endDate", "12/19/2020");
				put("categories", categories);
				put("dayLoad", "CALM");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		Map<String, Object> moreDetails = new HashMap<String, Object>() {
			{
				put("trip", trip);
			}
		};
		
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
		
		JSONObject route = new JSONObject( (Map) tripInfo.get("route"));
	
		int total = 0;
		int match = 0;
		
		for (int i = 1; i <= route.length(); i++) {
			JSONArray day = route.getJSONArray(String.valueOf(i));
			
			for (int j = 0; j < day.length(); j++) {
				JSONObject event = day.getJSONObject(j);
				String label = event.getString("label");
				
				total++;
				if( label.equals(categories[0]) || label.equals(categories[1])) {
					match++;
				}
			}
		}
		assertThat((match/total) >= 0.75);			
	}
	
	@Test
	public void testGenerateDataCollectionAndCheckCategories() throws JSONException {
		String[] categories = { "culture", "hiking", "art"};
		Map<String, Object> trip = new HashMap<String, Object>() {
			{
				put("startDate", "12/17/2020");
				put("endDate", "12/20/2020");
				put("categories", categories);
				put("dayLoad", "CALM");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		
		Map<String, Object> moreDetails = new HashMap<String, Object>() {{put("trip", trip);}};
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
		
		JSONObject route = new JSONObject( (Map) tripInfo.get("route"));
		ArrayList<String> cat = new ArrayList<>();
		cat.add(categories[0]);
		cat.add(categories[1]);
		cat.add(categories[2]);
		int match = 0;
		
		for (int i = 1; i <= route.length(); i++) {
			JSONArray day = route.getJSONArray(String.valueOf(i));
			for (int j = 0; j < day.length(); j++) {
				JSONObject event = day.getJSONObject(j);
				String label = event.getString("label");
				System.out.println(label);
				if(cat.contains(label)) {
					match++;
					cat.remove(label);
				}
			}
		}
		assertThat((match/categories.length) >= 0.70);			
	}
	
	@Test 
	public void testGenerateTripAndCheckDayLoad() throws JSONException {
		String[] categories = { "culture", "hiking", "art"};
		Map<String, Object> trip = new HashMap<String, Object>() {
			{
				put("startDate", "12/17/2020");
				put("endDate", "12/20/2020");
				put("categories", categories);
				put("dayLoad", "CALM");
				put("startLocation", "53.471557,-2.247717");
				put("endLocation", "53.371833,-1.466437");
			}
		};
		
		Map<String, Object> moreDetails = new HashMap<String, Object>() {{put("trip", trip);}};
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
		
		JSONObject route = new JSONObject( (Map) tripInfo.get("route"));
		
		for (int i = 1; i <= route.length(); i++) {
			JSONArray day = route.getJSONArray(String.valueOf(i));
			assertThat(day.length() <= 2); // 2 = CALM
		}	
	}
}
