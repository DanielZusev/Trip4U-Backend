package com.project.trip4u.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.trip4u.entity.UserEntity;


@Repository
public interface UserDao extends MongoRepository<UserEntity, String>{
	
	public List<UserEntity> findByUsername_FirstName(String firstName);
	
	public List<UserEntity> findByUsername_LastName(String lastName);
	
	public List<UserEntity> findByRole(String role);
	
	public Optional<UserEntity> findByEmail(String email);
	
}
