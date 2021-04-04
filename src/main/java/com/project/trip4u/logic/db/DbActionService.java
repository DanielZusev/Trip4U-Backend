package com.project.trip4u.logic.db;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip4u.boundary.ActionBoundary;
import com.project.trip4u.boundaryUtils.TripInfo;
import com.project.trip4u.converter.ActionEntityConverter;
import com.project.trip4u.dao.ActionDao;
import com.project.trip4u.dao.TripDao;
import com.project.trip4u.dao.UserDao;
import com.project.trip4u.data.ActionType;
import com.project.trip4u.data.UserRole;
import com.project.trip4u.entity.UserEntity;
import com.project.trip4u.exception.InternalServerException;
import com.project.trip4u.exception.NotFoundException;
import com.project.trip4u.exception.UnauthorizedException;
import com.project.trip4u.logic.actionUtils.ClientActions;
import com.project.trip4u.service.ActionService;

@Service
public class DbActionService implements ActionService{
	
	private ActionDao actionDao;
	private UserDao userDao;
	private TripDao tripDao;
	private ActionEntityConverter actionEntityConverter;
	
	@Autowired
	public DbActionService(ActionDao actionDao, UserDao userDao, 
			TripDao tripDao,
			ActionEntityConverter actionEntityConverter) {
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.tripDao = tripDao;
		this.actionEntityConverter = actionEntityConverter;
	}
	
	@Override
	public Object invokeAction(ActionBoundary action) throws ParseException {
		if( action.getInvokeBy() == null) 
			throw new InternalServerException("InvokeBy must not be null");
		
		if(action.getType() == null || action.getType().toString().isEmpty()) 
			throw new InternalServerException("Type must not be null");
		
		if(action.getType() == ActionType.GENERATE || action.getType() == ActionType.CREATE) {
			String tripKey = UUID.randomUUID().toString();
			action.setElementId(tripKey);
		}
		
		
		String userId = action.getInvokeBy();
		UserEntity userEntity = this.userDao.findByEmail(userId)
				.orElseThrow(() -> new NotFoundException("User With This Email Not Exist"));
		
		if(action.getType() == ActionType.UPDATE || action.getType() == ActionType.DELETE || action.getType() == ActionType.EDIT) {
			if(action.getElementId() == null) // element id = trip id
				throw new InternalServerException("Trip Id must not be null");
			
			String elementId = action.getElementId();
			TripInfo tripInfo = this.tripDao.findByTripId(elementId)
					.orElseThrow(() -> new NotFoundException("Trip With This Id Not Exist"));
			if(!userEntity.getRole().equals(UserRole.ADMIN.toString())) {
				if(!userEntity.getEmail().equals(tripInfo.getUserId())) {
					throw new UnauthorizedException("You don't have permission to do that");
				}
			}
		}
		
		String key = UUID.randomUUID().toString();
		action.setId(key);
		action.setTimeStamp(new Date());
		this.actionDao.save(this.actionEntityConverter.toEntity(action));
		return ClientActions.actionInvoker(action);

	}

	

	@Override
	public List<ActionBoundary> getAllActions(String adminEmail) {
		UserEntity invoker = this.userDao.findById(adminEmail).orElseThrow(() -> new NotFoundException("Admin doesn't exist"));
		if(!invoker.getRole().equals(UserRole.ADMIN.toString())) {
			throw new UnauthorizedException("You don't have permissions to do that");
		}
		return StreamSupport.stream(this.actionDao.findAll().spliterator(), false).map(this.actionEntityConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminEmail) {
		UserEntity invoker = this.userDao.findById(adminEmail).orElseThrow(() -> new NotFoundException("Admin doesn't exist"));
		if(!invoker.getRole().equals(UserRole.ADMIN.toString())) {
			throw new UnauthorizedException("You don't have permissions to do that");
		}
		this.actionDao.deleteAll();
	}
	
	
}
