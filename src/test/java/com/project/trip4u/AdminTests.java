package com.project.trip4u;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

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
import com.project.trip4u.data.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminTests {

	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String adminEmail;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/admin";
		this.adminEmail = "admin@gmail.com";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void addAdminToDB() {
		NewUserBoundary input = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
	}

	@AfterEach
	public void teardown() { // Admin URL : Delete All Users, action and trips
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/actions/{adminEmail}", this.adminEmail);
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/trips/{adminEmail}", this.adminEmail);
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}

	@Test
	public void testGetAllUsersAfterDatabaseIsInitializedWith5UsersWithoutAdmin() {
		// GIVEN the database contains 4 users
		// POST /users

		List<UserBoundary> databaseContent = IntStream.range(1, 5).mapToObj(i -> "User" + i)
				.map(newUser -> new NewUserBoundary("TOURIST", new UserName("First" + newUser, "Last" + newUser),
						newUser + "@gmail.com", "123456"))
				.map(newUser -> this.restTemplate.postForObject("http://localhost:" + this.port + "/users", newUser,
						UserBoundary.class))
				.collect(Collectors.toList());

		// WHEN I GET /admin/users/{adminEmail}
		String url = this.url + "/users/{adminEmail}";
		UserBoundary[] result = this.restTemplate.getForObject(url, UserBoundary[].class, this.adminEmail);

		List<UserBoundary> users = Arrays.stream(result).filter(user -> user.getRole() != UserRole.ADMIN)
				.collect(Collectors.toList());

		// THEN the server returns status 2xx
		// AND the response contains exact 4 users in the database without admin
		assertThat(users).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(databaseContent);
	}

	@Test
	public void testGetAllUsersAfterDatabaseIsInitializedWith5UsersWithoutAdminWithoutPermissions() {
		// GIVEN the database contains 4 users
		// POST /users
		List<UserBoundary> databaseContent = IntStream.range(1, 5).mapToObj(i -> "User" + i)
				.map(newUser -> new NewUserBoundary("TOURIST", new UserName("First" + newUser, "Last" + newUser),
						newUser + "@gmail.com", "123456"))
				.map(newUser -> this.restTemplate.postForObject("http://localhost:" + this.port + "/users", newUser,
						UserBoundary.class))
				.collect(Collectors.toList());

		// WHEN I GET /admin/users/{adminEmail}
		String url = this.url + "/users/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(url, UserBoundary[].class, "User1@gmail.com"));
	}

	@Test
	public void testDeleteAllUsersAfterDatabaseIsInitializedWith5Users() { //TODO line 125
		// GIVEN the database contains 4 users
		// POST /users
		List<UserBoundary> databaseContent = IntStream.range(1, 5).mapToObj(i -> "User" + i)
				.map(newUser -> new NewUserBoundary("TOURIST", new UserName("First" + newUser, "Last" + newUser),
						newUser + "@gmail.com", "123456"))
				.map(newUser -> this.restTemplate.postForObject("http://localhost:" + this.port + "/users", newUser,
						UserBoundary.class))
				.collect(Collectors.toList());

		// WHEN I DELETE /admin/users/{adminEmail}
		String url = this.url + "/users/{adminEmail}";
		this.restTemplate.delete(url, this.adminEmail);

		// WHEN I GET /admin/users/{adminEmail}
		addAdminToDB();
		UserBoundary[] result = this.restTemplate.getForObject(url, UserBoundary[].class, this.adminEmail);
		List<UserBoundary> users = Arrays.stream(result).filter(user -> user.getRole() != UserRole.ADMIN)
				.collect(Collectors.toList());

		// THEN the server returns status 2xx
		// AND the response contains exact 0 users in the database without admin
		assertThat(users).hasSize(0);
	}

	@Test
	public void testDeleteAllUsersAfterDatabaseIsInitializedWith5UsersWithoutPermissions() {
		// GIVEN the database contains 4 users
		// POST /users
		List<UserBoundary> databaseContent = IntStream.range(1, 5).mapToObj(i -> "User" + i)
				.map(newUser -> new NewUserBoundary("TOURIST", new UserName("First" + newUser, "Last" + newUser),
						newUser + "@gmail.com", "123456"))
				.map(newUser -> this.restTemplate.postForObject("http://localhost:" + this.port + "/users", newUser,
						UserBoundary.class))
				.collect(Collectors.toList());

		// WHEN I DELETE /admin/users/{adminEmail}
		String url = this.url + "/users/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.delete(url, "User1@gmail.com"));
	}

	@Test
	public void testGetAllActionsAfterDatabaseIsInitializedWith2Actions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i)
				.map(newAction -> new ActionBoundary(null, ActionType.CREATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		// WHEN I GET /admin/actions/{adminEmail}
		String url = this.url + "/actions/{adminEmail}";
		ActionBoundary[] actions = this.restTemplate.getForObject(url, ActionBoundary[].class, this.adminEmail);

		// THEN the server returns status 2xx
		// AND the response contains exact 2 actions in the database
		assertThat(actions).hasSize(2);
	}

	@Test
	public void testGetAllActionsAfterDatabaseIsInitializedWith2ActionsWithoutPermissions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i)
				.map(newAction -> new ActionBoundary(null, ActionType.CREATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com",
				"123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);

		// WHEN I GET /admin/actions/{adminEmail}
		String url = this.url + "/actions/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(url, ActionBoundary[].class, input.getEmail()));
	}

	@Test
	public void testDeleteAllActionsAfterDatabaseIsInitializedWith2Actions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i)
				.map(newAction -> new ActionBoundary(null, ActionType.CREATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		// WHEN I DELETE /admin/actions/{adminEmail}
		String url = this.url + "/actions/{adminEmail}";
		this.restTemplate.delete(url, this.adminEmail);

		// WHEN I GET /admin/action/{adminEmail}
		ActionBoundary[] actions = this.restTemplate.getForObject(url, ActionBoundary[].class, this.adminEmail);

		// THEN the server returns status 2xx
		// AND the response contains exact 0 actions in the database
		assertThat(actions).hasSize(0);
	}

	@Test
	public void testDeleteAllActionsAfterDatabaseIsInitializedWith2ActionsWithoutPermissions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i)
				.map(newAction -> new ActionBoundary(null, ActionType.CREATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
		
		// WHEN I DELETE /admin/actions/{adminEmail}
		String url = this.url + "/actions/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.delete(url, input.getEmail()));
	}

	@Test
	public void testGetAllTripsAfterDatabaseIsInitializedWith2Trips() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i).map(
				newAction -> new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		// WHEN I GET /admin/trips/{adminEmail}
		String url = this.url + "/trips/{adminEmail}";
		TripInfo[] trips = this.restTemplate.getForObject(url, TripInfo[].class, this.adminEmail);

		// THEN the server returns status 2xx
		// AND the response contains exact 2 actions in the database
		assertThat(trips).hasSize(2);
	}

	@Test
	public void testGetAllTripsAfterDatabaseIsInitializedWith2TripsWithoutPermissions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i).map(
				newAction -> new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
		
		// WHEN I GET /admin/trips/{adminEmail}
		String url = this.url + "/trips/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(url, TripInfo[].class, input.getEmail()));
	}

	@Test
	public void testDeleteAllTripsAfterDatabaseIsInitializedWith2Trips() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i).map(
				newAction -> new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		// WHEN I DELETE /admin/trips/{adminEmail}
		String url = this.url + "/trips/{adminEmail}";
		this.restTemplate.delete(url, this.adminEmail);

		// WHEN I GET /admin/trips/{adminEmail}
		TripInfo[] trips = this.restTemplate.getForObject(url, TripInfo[].class, this.adminEmail);

		// THEN the server returns status 2xx
		// AND the response contains exact 2 actions in the database
		assertThat(trips).hasSize(0);
	}

	@Test
	public void testDeleteAllTripsAfterDatabaseIsInitializedWith2TripsWithoutPermissions() {
		// GIVEN the database contains 2 actions
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
		List<ActionBoundary> databaseContent = IntStream.range(1, 3).mapToObj(i -> "Action" + i).map(
				newAction -> new ActionBoundary(null, ActionType.GENERATE, null, null, this.adminEmail, moreDetails))
				.map(newAction -> this.restTemplate.postForObject("http://localhost:" + this.port + "/actions",
						newAction, ActionBoundary.class))
				.collect(Collectors.toList());

		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
		
		// WHEN I DELETE /admin/trips/{adminEmail}
		String url = this.url + "/trips/{adminEmail}";

		// THEN the server returns status 4xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(url, TripInfo[].class, input.getEmail()));
	}
}
