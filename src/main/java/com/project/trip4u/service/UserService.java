package com.project.trip4u.service;

import java.util.List;

import com.project.trip4u.boundary.UserBoundary;


public interface UserService {
	
	public UserBoundary createUser(UserBoundary user);
	public UserBoundary login(String userEmail, String password);
	public UserBoundary updateUser(String userEmail, UserBoundary update);
	public List<UserBoundary> getAllUsers(String admainEmail);
	public void deleteAllUsers(String adminEmail);

}
