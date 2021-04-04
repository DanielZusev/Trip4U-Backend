package com.project.trip4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundary.UserBoundary;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.service.ActionService;
import com.project.trip4u.service.TripService;
import com.project.trip4u.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {

	private UserService userService;
	private ActionService actionService;
	private TripService tripService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}

	@Autowired
	public void setTripService(TripService tripService) {
		this.tripService = tripService;
	}
	
	@RequestMapping(path = "/admin/users/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("adminEmail") String adminEmail) {
		this.userService.deleteAllUsers(adminEmail);
	}

	@RequestMapping(path = "/admin/trips/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllTrips(@PathVariable("adminEmail") String adminEmail) {
		tripService.deleteAllTrips(adminEmail);
	}

	@RequestMapping(path = "/admin/actions/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("adminEmail") String adminEmail) {
		actionService.deleteAllActions(adminEmail);
	}

	@RequestMapping(path = "/admin/users/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllusers(@PathVariable("adminEmail") String adminEmail) {

		return userService.getAllUsers(adminEmail).toArray(new UserBoundary[0]);
	}

	@RequestMapping(path = "/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminEmail") String adminEmail) {
		return actionService.getAllActions(adminEmail).toArray(new ActionBoundary[0]);
	}

	@RequestMapping(path = "/admin/trips/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public TripInfo[] exportAllTrips(@PathVariable("adminEmail") String adminEmail) {
		return tripService.getAllTrips(adminEmail).toArray(new TripInfo[0]);
	}

}
