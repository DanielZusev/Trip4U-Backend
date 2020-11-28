package com.project.trip4u.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.trip4u.boundaryUtils.UserName;


@Document(collection = "users")
public class UserEntity {
	
	@Id
	private String email;
	private UserName username;
	private String password;
	private String role;
	
	public UserEntity() {
		
	}

	public UserEntity(String email, UserName username, String password, String role) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserName getUsername() {
		return username;
	}

	public void setUsername(UserName username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
