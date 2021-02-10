package com.project.trip4u.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.converter.ActionEntityConverter;
import com.project.trip4u.dao.ActionDao;
import com.project.trip4u.dao.ElementDao;
import com.project.trip4u.dao.UserDao;
import com.project.trip4u.data.ActionType;
import com.project.trip4u.entity.ElementEntity;
import com.project.trip4u.entity.UserEntity;
import com.project.trip4u.exception.InternalServerException;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.logic.actionUtils.ClientActions;
import com.project.trip4u.service.ActionService;

@Service
public class DbActionService implements ActionService{
	
	private ActionDao actionDao;
	private UserDao userDao;
	private ElementDao elementDao;
	private ActionEntityConverter actionEntityConverter;
	
	@Autowired
	public DbActionService(ActionDao actionDao, UserDao userDao, ElementDao elementDao,ActionEntityConverter actionEntityConverter) {
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.actionEntityConverter = actionEntityConverter;
	}
	
	@Override
	public Object invokeAction(ActionBoundary action) {
		if( action.getInvokeBy() == null) 
			throw new InternalServerException("InvokeBy must not be null");
		
		if(action.getType() == null || action.getType().toString().isEmpty()) 
			throw new InternalServerException("Type must not be null");
		
		if( action.getElementId() == null) 
			throw new InternalServerException("Element Id must not be null");
		
		String userId = action.getInvokeBy();
		UserEntity userEntity = this.userDao.findByEmail(userId)
				.orElseThrow(() -> new NotFoundException("User With This Email Not Exist"));
		
		if(action.getType() == ActionType.UPDATE || action.getType() == ActionType.DELETE) {
			String elementId = action.getElementId();
			ElementEntity elementEntity = this.elementDao.findByElementId(elementId)
					.orElseThrow(() -> new NotFoundException("Element With This Id Not Exist"));
		}
		
		String key = UUID.randomUUID().toString();
		action.setId(key);
		action.setTimeStamp(new Date());
		this.actionDao.save(this.actionEntityConverter.toEntity(action));
		return ClientActions.actionInvoker(action);

	}

	

	@Override
	public List<ActionBoundary> getAllActions(String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllActions(String adminEmail) {
		// TODO Auto-generated method stub
		
	}
	
	
}
