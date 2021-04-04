//package com.project.trip4u.converter;
//
//import org.springframework.stereotype.Component;
//
//import com.project.trip4u.boundary.ElementBoundary;
//import com.project.trip4u.entity.ElementEntity;
//
//@Component
//public class ElementEntityConverter {
//
//	
//	public ElementBoundary fromEntity(ElementEntity elementEntity) {
//
//		return new ElementBoundary(
//				elementEntity.getElementId(),
//				elementEntity.getUserId(),
//				elementEntity.getType(),
//				elementEntity.getName(),
//				elementEntity.getMoreDetails());	
//	}
//	
//	public ElementEntity toEntity(ElementBoundary elementBoundary) {
//		
//		ElementEntity elementEntity = new ElementEntity();
//		
//		elementEntity.setElementId(elementBoundary.getElementId());
//		elementEntity.setUserId(elementBoundary.getUserId());
//		elementEntity.setType(elementBoundary.getType());
//		elementEntity.setName(elementBoundary.getName());
//		elementEntity.setMoreDetails(elementBoundary.getMoreDetails());
//		
//		return elementEntity;
//	}
//}
