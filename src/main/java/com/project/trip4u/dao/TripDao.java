package com.project.trip4u.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.trip4u.boundaryUtils.TripInfo;

@Repository 
public interface TripDao extends MongoRepository<TripInfo, String>{
	
	public Optional<TripInfo> findByTripId(String tripId);
	
	public List<TripInfo> findByUserId(String userId);

}
