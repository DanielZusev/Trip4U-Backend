package com.project.trip4u.logic.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.ValidEmail;
import com.project.trip4u.converter.UserEntityConverter;
import com.project.trip4u.dao.UserDao;
import com.project.trip4u.entity.UserEntity;
import com.project.trip4u.service.UserService;


@Service
public class DbUserService implements UserService{
	
	private UserDao userDao;
	private UserEntityConverter userEntityConverter;
	
	@Autowired
	public DbUserService(UserEntityConverter userEntityConverter, UserDao userDao) {
		this.userDao = userDao;
		this.userEntityConverter = userEntityConverter;
	}
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		if(user.getEmail() == null)
			throw new RuntimeException("Email must not be null");
		if(!ValidEmail.isValid(user.getEmail()))
			throw new RuntimeException("User email not valid");
		if(user.getRole() == null) 
			throw new RuntimeException("User Role cannot be null");
		if(user.getUsername().getFirstName() == null || user.getUsername().getLastName() == null)
			throw new RuntimeException("User name cannot be null");
		if(user.getPassword() == null) 
			throw new RuntimeException("User password cannot be null");

		
		if(userDao.findById(user.getEmail()).isPresent())
			throw new RuntimeException("User email is already exist");
		
		UserEntity userEntity = this.userEntityConverter.toEntity(user);
		return this.userEntityConverter.fromEntity(this.userDao.save(userEntity));
	}

	@Override
	public UserBoundary login(String userEmail, String password) {
		
		System.err.println("pass:" + password);
		
		Optional<UserEntity> optionalUserEntity = this.userDao.findByEmail(userEmail);
		if(!optionalUserEntity.isPresent())
			throw new RuntimeException("User With This Email Not Exist");
		UserEntity userEntity = optionalUserEntity.get();
		if(!userEntity.getPassword().equals(password))
			throw new RuntimeException("Wrong Password");
		
		return userEntityConverter.fromEntity(userEntity);
	}

	@Override
	public UserBoundary updateUser(String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String admainEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers(String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
