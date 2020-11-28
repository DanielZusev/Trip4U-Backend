package com.project.trip4u.boundary;

import com.project.trip4u.boundaryUtils.UserName;
import com.project.trip4u.data.UserRole;

public class UserBoundary {
	
	private UserRole role;
	private UserName username;
	private String email;
	private String password;
	
	
	public UserBoundary() {
	
	}
	
	public UserBoundary(UserRole role, UserName username, String email, String password) {
		super();
		this.role = role;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
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
