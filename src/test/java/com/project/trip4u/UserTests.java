package com.project.trip4u;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.project.trip4u.boundary.NewUserBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.UserName;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.utils.PasswordEncoder;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests {

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
		this.url = "http://localhost:" + this.port + "/users";
		this.adminEmail = "admin@gmail.com";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void addAdminToDB() {
		NewUserBoundary input = new NewUserBoundary("ADMIN", new UserName("Admin", "Test"), adminEmail, "123456");
		this.restTemplate.postForObject(this.url, input, UserBoundary.class);
	}
	
	@AfterEach
	public void teardown() { // Admin URL : Delete All Users
		this.restTemplate.delete("http://localhost:" + this.port + "/admin/users/{adminEmail}", this.adminEmail);
	}
	
	@Test
	public void testUserSignUpAndRetreiveCorrectDetails() throws Exception {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users AND send a new user boundary
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);

		// THEN the server returns status 2xx
		// AND retrieves a user boundary with correct details
		if (!output.getEmail().equals(input.getEmail())) {
			throw new Exception("Email doesn't match");
		}
		if (!output.getUsername().getFirstName().equals(input.getUsername().getFirstName()) || !output.getUsername().getLastName().equals(input.getUsername().getLastName())) {
			throw new Exception("Username doesn't match");
		}
		if (!output.getRole().toString().equals(input.getRole())) {
			throw new Exception("Role doesn't match");
		}
		if (!new PasswordEncoder().match(output.getEmail(), input.getPassword(), output.getPassword())) {
			throw new Exception("Password doesn't match");
		}
	}
	
	@Test
	public void testUserLoginAndRetreiveCorrectDetails() throws Exception {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users AND send a new user boundary AND I GET /users/login/{userEmail}/{userPassword}
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		UserBoundary newOutput = this.restTemplate.getForObject(this.url + "/login/{userEmail}/{userPassword}", UserBoundary.class, input.getEmail(), input.getPassword());
		// THEN the server return status 2xx
		// AND retrieves a user boundary with correct details
		assertThat(newOutput).usingRecursiveComparison().isEqualTo(output);
	}
	
	@Test
	public void testUserUpdateDetailsAndValidation() throws Exception {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users AND send a new user boundary AND I PUT /users/{userEmail} AND send a update user boundary
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		UserBoundary updatedInput = new UserBoundary();
		// NOTICE - actually user can't change his email and role
		updatedInput.setEmail(output.getEmail());
		updatedInput.setUsername(new UserName("Test", "Testy"));
		updatedInput.setPassword("111111");
		updatedInput.setRole(UserRole.TOURIST);
		this.restTemplate.put(this.url + "/{userEmail}", updatedInput, output.getEmail());
		UserBoundary expectedOutput = new UserBoundary(updatedInput.getRole(), updatedInput.getUsername(), updatedInput.getEmail(), new PasswordEncoder().encode(updatedInput.getEmail(), updatedInput.getPassword()));
		// THEN the server return status 2xx
		// AND the database is updated with the new values
		UserBoundary updatedOutput = this.restTemplate.getForObject(this.url + "/login/{userEmail}/{userPassword}", UserBoundary.class, input.getEmail(), updatedInput.getPassword());
		assertThat(updatedOutput).usingRecursiveComparison().isEqualTo(expectedOutput);
	}
	
	@Test
	public void testUserSignUpWithEmptyEmail() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with empty mail
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), null, "123456");
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpWithInvalidEmail() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with empty mail
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "sdfdfsdf", "123456");
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpWithEmptyRole() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with empty mail
		NewUserBoundary input = new NewUserBoundary(null, new UserName("First", "Last"), "test@gmail.com", "123456");
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpWithEmptyPassword() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with empty mail
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", null);
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpWithEmailThatAlreadyExistInDB() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with empty mail
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		NewUserBoundary input2 = new NewUserBoundary("TOURIST", new UserName("Other", "User"), "test@gmail.com", "123456");
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input2, UserBoundary.class));
	}
	
	@Test
	public void testUserLoginWithNonExistingEmailInDB() {
		String email = "dan@gmail.com";
		String pass = "123123";
			
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(this.url + "/login/" + email + "/" + pass, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpWithInvalidRole() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /users and send a new user boundary with invalid role 
		NewUserBoundary input = new NewUserBoundary("TRIP4U", new UserName("First", "Last"), "test@gmail.com", "123456");
		// THEN the sever return status 5xx
		// AND throws exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, UserBoundary.class));
	}
	
	@Test
	public void testUserSignUpAndCheckThatPasswordEncrypted() throws Exception {
		NewUserBoundary input = new NewUserBoundary("TOURIST", new UserName("First", "Last"), "test@gmail.com", "123456");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
	
		assertNotEquals("123456", output.getPassword());
	}
}
