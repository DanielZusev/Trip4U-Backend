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
public class TripTests {
	
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
		this.url = "http://localhost:" + this.port + "/trips";
		this.adminEmail = "admin@gmail.com";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void addAdminToDB() {
		NewUserBoundary input = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/users", input, UserBoundary.class);
	}
	
	@AfterEach
	public void teardown() { // Admin URL : Delete All Users
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}

}
