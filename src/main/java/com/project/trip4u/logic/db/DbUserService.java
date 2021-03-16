package com.project.trip4u.logic.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.ValidEmail;
import com.project.trip4u.converter.UserEntityConverter;
import com.project.trip4u.dao.UserDao;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.entity.UserEntity;
import com.project.trip4u.exception.InternalServerException;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.exception.UnauthorizedException;
import com.project.trip4u.service.UserService;
import com.project.trip4u.utils.PasswordEncoder;

@Service
public class DbUserService implements UserService {

	private UserDao userDao;
	private UserEntityConverter userEntityConverter;

	@Autowired
	public DbUserService(UserEntityConverter userEntityConverter, UserDao userDao) {
		this.userDao = userDao;
		this.userEntityConverter = userEntityConverter;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		if (user.getEmail() == null)
			throw new InternalServerException("Email must not be null");
		if (!ValidEmail.isValid(user.getEmail()))
			throw new InternalServerException("User email not valid");
		if (user.getRole() == null)
			throw new InternalServerException("User Role cannot be null");
		if (user.getUsername().getFirstName() == null || user.getUsername().getLastName() == null)
			throw new InternalServerException("User name cannot be null");
		if (user.getPassword() == null)
			throw new InternalServerException("User password cannot be null");
		else
			user.setPassword((new PasswordEncoder()).encode(user.getEmail(), user.getPassword()));

		if (userDao.findById(user.getEmail()).isPresent())
			throw new InternalServerException("User email is already exist");

		UserEntity userEntity = this.userEntityConverter.toEntity(user);
		return this.userEntityConverter.fromEntity(this.userDao.save(userEntity));
	}

	@Override
	public UserBoundary login(String userEmail, String password) {

		Optional<UserEntity> optionalUserEntity = this.userDao.findByEmail(userEmail);
		if (!optionalUserEntity.isPresent())
			throw new NotFoundException("User With This Email Not Exist");
		UserEntity userEntity = optionalUserEntity.get();
		if (!new PasswordEncoder().match(userEmail, password, userEntity.getPassword()))
			throw new NotFoundException("Wrong Password");

		return userEntityConverter.fromEntity(userEntity);
	}

	@Override
	public UserBoundary updateUser(String userEmail, UserBoundary update) {

		boolean dirtyFlag = false;
		Optional<UserEntity> optionalUserEntity = this.userDao.findById(userEmail);
		if (!optionalUserEntity.isPresent())
			throw new NotFoundException("no user exist for this user Email: " + userEmail);

		UserBoundary existing = userEntityConverter.fromEntity(optionalUserEntity.get());

		if (update.getUsername() != null) {
			dirtyFlag = true;
			existing.setUsername(update.getUsername());
		}

		if (update.getRole() != null) {
			if (update.getRole().equals(UserRole.TOURIST) || update.getRole().equals(UserRole.BUSSINESS_MANAGER)
					|| update.getRole().equals(UserRole.ADMIN)) {
				dirtyFlag = true;
				existing.setRole(update.getRole());
			}
		}

		if (update.getPassword() != null) {
			dirtyFlag = true;
			existing.setPassword(new PasswordEncoder().encode(update.getEmail(), update.getPassword()));
		}

		if (dirtyFlag)
			this.userDao.save(userEntityConverter.toEntity(existing));
		return existing;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminEmail) {

		Optional<UserEntity> optionalUserEntity = this.userDao.findByEmail(adminEmail);
		if (!optionalUserEntity.isPresent())
			throw new NotFoundException("User With This Email Not Exist");

		UserBoundary admin = userEntityConverter.fromEntity(optionalUserEntity.get());

		if (!admin.getRole().equals(UserRole.ADMIN)) {
			throw new UnauthorizedException("This user email has no Admin permissions");
		}

		return StreamSupport.stream(this.userDao.findAll().spliterator(), false)
				.map(this.userEntityConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminEmail) {

		Optional<UserEntity> optionalUserEntity = this.userDao.findByEmail(adminEmail);
		if (!optionalUserEntity.isPresent())
			throw new NotFoundException("User With This Email Not Exist");

		UserBoundary admin = userEntityConverter.fromEntity(optionalUserEntity.get());

		if (!admin.getRole().equals(UserRole.ADMIN)) {
			throw new UnauthorizedException("Only Admin Can Delete All Users");
		}

		this.userDao.deleteAll();
	}

}
