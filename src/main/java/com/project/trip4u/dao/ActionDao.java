package com.project.trip4u.dao;

import java.util.Date;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.trip4u.entity.ActionEntity;


@Repository 
public interface ActionDao extends MongoRepository<ActionEntity, String>{

	public List<ActionEntity> findByActionId(String actionId);
	
	public List<ActionEntity> findByElementId(String elementId);
	
	public List<ActionEntity> findByInvokeBy(String invokeBy);
	
	public List<ActionEntity> findByTimeStamp(Date timeStamp);
	
	public List<ActionEntity> findByType(String type);
	
	//TODO add queries for more details
}
