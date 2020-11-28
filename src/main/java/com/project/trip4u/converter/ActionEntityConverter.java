package com.project.trip4u.converter;

import org.springframework.stereotype.Component;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.data.ActionType;
import com.project.trip4u.entity.ActionEntity;



@Component
public class ActionEntityConverter {

	public ActionBoundary fromEntity(ActionEntity actionEntity) {

		return new ActionBoundary(
				actionEntity.getId(),
				ActionType.valueOf(actionEntity.getType()),
				actionEntity.getElementId(),
				actionEntity.getTimeStamp(),
				actionEntity.getInvokeBy(),
				actionEntity.getMoreDetails());	
	}
	
	public ActionEntity toEntity(ActionBoundary actionBoundary) {
		
		ActionEntity actionEntity = new ActionEntity();
		
		actionEntity.setId(actionBoundary.getId());
		actionEntity.setType(actionBoundary.getType().toString());
		actionEntity.setElementId(actionBoundary.getElementId());
		actionEntity.setTimeStamp(actionBoundary.getTimeStamp());
		actionEntity.setInvokeBy(actionBoundary.getInvokeBy());
		actionEntity.setMoreDetails(actionBoundary.getMoreDetails());

		return actionEntity;
	}
}
