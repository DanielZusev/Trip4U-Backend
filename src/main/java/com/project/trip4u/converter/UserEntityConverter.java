package com.project.trip4u.converter;

import org.springframework.stereotype.Component;

import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.entity.UserEntity;

@Component
public class UserEntityConverter {
	
	public UserBoundary fromEntity(UserEntity userEntity) {

		return new UserBoundary(
				UserRole.valueOf(userEntity.getRole()),
				userEntity.getUsername(),
				userEntity.getEmail(),
				userEntity.getPassword());	
	}
	
	public UserEntity toEntity(UserBoundary userBoundary) {
		
		UserEntity userEntity = new UserEntity();
		
		userEntity.setEmail(userBoundary.getEmail());
		userEntity.setPassword(userBoundary.getPassword());
		userEntity.setUsername(userBoundary.getUsername());
		userEntity.setRole(userBoundary.getRole().toString());
		
		return userEntity;
	}
}
