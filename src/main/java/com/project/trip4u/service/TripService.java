package com.project.trip4u.service;

import java.util.List;

import com.project.trip4u.boundaryUtils.TripInfo;

public interface TripService {

	public List<TripInfo> getAllTrips(String adminEmail);

	public void deleteAllTrips(String adminEmail);
}
