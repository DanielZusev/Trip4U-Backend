package com.project.trip4u.boundary;

import com.project.trip4u.boundaryUtils.UserName;


public class NewUserBoundary {

	private String role;
	private UserName username;
	private String email;
	private String password;
	
	public NewUserBoundary() {
	
	}

	public NewUserBoundary(String role, UserName username, String email, String password) {
		this.role = role;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserName getUsername() {
		return username;
	}

	public void setUsername(UserName username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
