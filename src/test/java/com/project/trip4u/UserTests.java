package com.project.trip4u;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests {

	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
