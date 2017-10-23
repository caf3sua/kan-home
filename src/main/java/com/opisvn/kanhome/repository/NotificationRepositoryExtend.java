package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.Notification;



@Repository
public interface NotificationRepositoryExtend {

	List<Notification> findFromId(String id);
	
	List<Notification> findAll();
	
	Long getMaxId();
	
	Notification findOne(Long id);
}
