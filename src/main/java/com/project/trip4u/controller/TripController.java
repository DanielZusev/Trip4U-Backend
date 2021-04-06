package com.project.trip4u.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.service.TripService;

@RestController
@CrossOrigin(origins = "*")
public class TripController {

	private TripService tripService;
	
	@Autowired
	public void setTripService(TripService tripService) {
		this.tripService = tripService;
	}

	@RequestMapping(path = "/trips/{userEmail}/{tripId}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public TripInfo getElement(@PathVariable("userEmail") String userEmail,
			@PathVariable("tripId") String tripId) {

		return tripService.getByTripId(userEmail, tripId);
	}

	@RequestMapping(path = "/trips/{userEmail}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public List<TripInfo> getAllElements(@PathVariable("userEmail") String userEmail) {

		return tripService.getAllByUserId(userEmail);
	}

}
