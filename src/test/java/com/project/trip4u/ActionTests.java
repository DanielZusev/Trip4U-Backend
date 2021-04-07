package com.project.trip4u;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.project.trip4u.boundary.NewUserBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.UserName;

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
	public void addAdminToDB() {
		NewUserBoundary admin = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		NewUserBoundary tourist = new NewUserBoundary("TOURIST", new UserName("Tourist", "Test"), touristEmail, "123456");
		NewUserBoundary bManager = new NewUserBoundary("BUSSINESS_MANAGER", new UserName("Manager", "Test"), bManagerEmail, "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", admin, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", tourist, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", bManager, UserBoundary.class);
	}
	
	@AfterEach
	public void teardown() { // Admin URL : Delete All Users
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}
}
