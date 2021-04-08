package com.project.trip4u;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.project.trip4u.data.DayLoad;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {

	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String adminEmail;
	private String touristEmail;
	private String bManagerEmail;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/actions";
		this.adminEmail = "admin@gmail.com";
		this.touristEmail = "tourist@gmail.com";
		this.bManagerEmail = "bManager@gmail.com";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void addUsersToDB() {
		NewUserBoundary admin = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		NewUserBoundary tourist = new NewUserBoundary("TOURIST", new UserName("Tourist", "Test"), touristEmail,
				"123456");
		NewUserBoundary bManager = new NewUserBoundary("BUSSINESS_MANAGER", new UserName("Manager", "Test"),
				bManagerEmail, "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", admin, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", tourist, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", bManager, UserBoundary.class);
	}

	@AfterEach
	public void teardown() { // Admin URL : Delete All Users
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}

	@Test
	public void testinvokeGenerateActionAndRetriveARoute() throws Exception {
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

		// THEN I get a route
		Map<String, Object> retrivedAction = this.restTemplate
				.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);

		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");

		assertThat(tripInfo.get("route")).isNotNull();

	}

	@Test
	public void testInvokeCreateActionAndRetriveEventsPool() {
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
		ActionBoundary action = new ActionBoundary(null, ActionType.CREATE, null, null, this.adminEmail, moreDetails);

		// THEN I get events pool
		Map<String, Object> retrivedAction = this.restTemplate
				.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);

		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");

		assertThat(tripInfo.get("eventsPool")).isNotNull();
	}

	@Test
	public void testInvokeEditActionAndRetriveEventsPool() throws Exception {
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
		//THEN I edit the trip
		// POST /actions
		//THEN events pool not null
		ActionBoundary action2 = new ActionBoundary(null, ActionType.EDIT, tripInfo.get("tripId").toString(), null, this.adminEmail, moreDetails);
		Map<String, Object> retrivedAction2 = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action2, HashMap.class);
		Map<String, Object> tripInfo2 = (Map<String, Object>) retrivedAction2.get("trip");

		assertThat(tripInfo2.get("eventsPool")).isNotNull();

	}

	@Test
	public void testInvokeActionWithoutType() {
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
		ActionBoundary action = new ActionBoundary(null, null, null, null, this.adminEmail, moreDetails);
		// THEN the server returns status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate
				.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class));

	}
	
	@Test
	public void testInvokeUpdateAction() {
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
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.touristEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
		
		//THEN we edit the dayload of the trip
		trip.replace("dayLoad", "INTENSE");
		moreDetails.replace("trip", trip);
		//THEN POST changes
		ActionBoundary action2 = new ActionBoundary(null, ActionType.UPDATE, tripInfo.get("tripId").toString(), null, this.touristEmail, moreDetails);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action2, HashMap.class);
		//THEN check that changes have been made
		String url = "http://localhost:" + this.port + "/trips/{userEmail}/{tripId}";
		TripInfo tripInfo2 = this.restTemplate.getForObject(url, TripInfo.class, this.touristEmail,tripInfo.get("tripId").toString());
		
		assertThat(tripInfo2.getDayLoad().toString()).isEqualTo(DayLoad.INTENSE.toString());
		
	}
	
	@Test 
	public void testInvokeDeleteAction() {
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
		ActionBoundary action = new ActionBoundary(null, ActionType.GENERATE, null, null, this.touristEmail, moreDetails);
		Map<String, Object> retrivedAction = this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action, HashMap.class);
		Map<String, Object> tripInfo = (Map<String, Object>) retrivedAction.get("trip");
		
		ActionBoundary action2 = new ActionBoundary(null, ActionType.DELETE, tripInfo.get("tripId").toString(), null, this.touristEmail, moreDetails);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action2, HashMap.class);
		
		String url = "http://localhost:" + this.port + "/trips/{userEmail}/{tripId}";

		// THEN the server returns status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(url, TripInfo.class, this.touristEmail));
	}
		
	
	@Test
	public void testInvokeDeleteActionOnTripThatNotBelongToMe() {
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
		
		ActionBoundary action2 = new ActionBoundary(null, ActionType.DELETE, tripInfo.get("tripId").toString(), null, this.touristEmail, moreDetails);
		;
		// THEN the server returns status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action2, HashMap.class));
		
	}
	
	@Test
	public void testInvokeUpdateActionOnTripThatNotBelongToMe() {
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
		
		//THEN we edit the dayload of the trip
		trip.replace("dayLoad", "INTENSE");
		moreDetails.replace("trip", trip);
		
		ActionBoundary action2 = new ActionBoundary(null, ActionType.UPDATE, tripInfo.get("tripId").toString(), null, this.touristEmail, moreDetails);
		
		assertThrows(Exception.class, () -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions", action2, HashMap.class));
		
		
		
	}
}