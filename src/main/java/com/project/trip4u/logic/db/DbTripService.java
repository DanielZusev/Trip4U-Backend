package com.project.trip4u.logic.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.dao.TripDao;
import com.project.trip4u.dao.UserDao;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.entity.UserEntity;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.exception.UnauthorizedException;
import com.project.trip4u.service.TripService;

@Service
public class DbTripService implements TripService{

	private TripDao tripDao;
	private UserDao userDao;
	
	@Autowired
	public DbTripService(TripDao tripDao, UserDao userDao) {
		this.tripDao = tripDao;
		this.userDao = userDao;
	}
	
	@Override
	public List<TripInfo> getAllTrips(String adminEmail) {
		UserEntity invoker = this.userDao.findById(adminEmail).orElseThrow(() -> new NotFoundException("Admin doesn't exist"));
		if (!invoker.getRole().equals(UserRole.ADMIN.toString())) {
			throw new UnauthorizedException("This user email has no Admin permissions");
		}
		return StreamSupport.stream(this.tripDao.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void deleteAllTrips(String adminEmail) {
		UserEntity invoker = this.userDao.findById(adminEmail).orElseThrow(() -> new NotFoundException("Admin doesn't exist"));
		if(!invoker.getRole().equals(UserRole.ADMIN.toString())) {
			throw new UnauthorizedException("You don't have permissions to do that");
		}
		this.tripDao.deleteAll();
		
	}

}
