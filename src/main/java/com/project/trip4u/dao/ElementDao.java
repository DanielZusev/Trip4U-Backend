package com.project.trip4u.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.trip4u.entity.ElementEntity;


@Repository 
public interface ElementDao extends MongoRepository<ElementEntity, String>{
	
	public Optional<ElementEntity> findByElementId(String elementId);
	
	public List<ElementEntity> findByUserId(String userId);
	
	public List<ElementEntity> findByName(String name);
	
	public List<ElementEntity> findByType(String type);
	
//TODO add queries for more details
	
	
}
