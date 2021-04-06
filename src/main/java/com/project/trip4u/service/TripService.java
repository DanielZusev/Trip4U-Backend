package com.project.trip4u.service;

import java.util.List;
import java.util.Optional;

import com.project.trip4u.boundaryUtils.TripInfo;

public interface TripService {

	public TripInfo getByTripId(String userId, String tripId);
	
	public List<TripInfo> getAllByUserId(String userId); 
	
	public List<TripInfo> getAllTrips(String adminEmail);

	public void deleteAllTrips(String adminEmail);
}
