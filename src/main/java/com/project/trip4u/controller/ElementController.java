//package com.project.trip4u.controller;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.project.trip4u.boundary.ElementBoundary;
//
//
//@RestController
//@CrossOrigin(origins = "*")
//public class ElementController {
//	
//	
//	@RequestMapping(path = "/elements/{adminEmail}",
//					method = RequestMethod.POST,
//					produces = MediaType.APPLICATION_JSON_VALUE,
//					consumes = MediaType.APPLICATION_JSON_VALUE)
//
//	public ElementBoundary createNewElement(@RequestBody ElementBoundary newElementBoundary, @PathVariable("adminEmail") String adminEmail) {
//			
//		return null;
//	}
//
//	@RequestMapping(path = "/elements/{adminEmail}/{elementId}",
//					method = RequestMethod.PUT,
//					consumes = MediaType.APPLICATION_JSON_VALUE)
//	
//	public void updateElement(@RequestBody ElementBoundary elementBoundary,
//			 @PathVariable("adminEmail") String adminEmail, @PathVariable("elementId") String elementId) {
//		
//			
//	}
//	
//	@RequestMapping(path = "/elements/{userEmail}/{elementId}",
//					method = RequestMethod.GET,
//					produces = MediaType.APPLICATION_JSON_VALUE)
//	
//	public ElementBoundary getElement( @PathVariable("userEmail") String userEmail, @PathVariable("elementId") String elementId) {
//		
//		return null;
//	}
//	
//	@RequestMapping(path = "/elements/{userEmail}",
//					method = RequestMethod.GET,
//					produces = MediaType.APPLICATION_JSON_VALUE)
//	
//	public ElementBoundary[] getAllElements(@PathVariable("userEmail") String userEmail) {//TODO pagination
//		
//		return null;
//	}
//	
//	@RequestMapping(path = "/elements/{adminEmail}/{elementId}/children",
//					method = RequestMethod.PUT,
//					consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void bindExistingElementToAnExistingChildElement(@RequestBody String childElementId,
//															@PathVariable("managerEmail") String adminEmail,
//															@PathVariable("elementId") String parentElementId) {
//		
//	
//	}
//	
//	@RequestMapping(path = "/elements/{userEmail}/{elementId}/children",
//					method = RequestMethod.GET,
//					produces = MediaType.APPLICATION_JSON_VALUE)
//	public ElementBoundary[] getAllChildrenOfAnExistingElement(@PathVariable("userEmail") String userEmail, @PathVariable("elementId") String parentElementId) {//TODO pagination
//		
//		return null;
//	}
// 	
//	
//	
//	@RequestMapping(path = "/elements/{userEmail}/search/byName/{name}",
//					method = RequestMethod.GET,
//					produces = MediaType.APPLICATION_JSON_VALUE)
//	public ElementBoundary[] SearchElementByName(@PathVariable("userEmail") String userEmail, @PathVariable("name") String name) {//TODO pagination
//		
//		return null;
//	}
//	
//	@RequestMapping(path = "/elements/{userEmail}/search/byType/{type}",
//					method = RequestMethod.GET,
//					produces = MediaType.APPLICATION_JSON_VALUE)
//	public ElementBoundary[] SearchElementByType(@PathVariable("userEmail") String userEmail, @PathVariable("type") String type) {//TODO pagination
//		
//		return null;
//	}
//	
//
//}
