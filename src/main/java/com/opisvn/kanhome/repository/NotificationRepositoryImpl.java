package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.Notification;

public class NotificationRepositoryImpl implements NotificationRepositoryExtend {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Notification> findFromId(String id) {
		Query query = new Query();
		Long fromId = Long.parseLong(id);
		query.addCriteria(Criteria.where("_id").gt(fromId));
		query.with(new Sort(Sort.Direction.DESC, "_id"));
		
		List<Notification> notifications = mongoTemplate.find(query, Notification.class);
		
		return notifications;
	}

	@Override
	public List<Notification> findAll() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "_id"));
		
		List<Notification> notifications = mongoTemplate.find(query, Notification.class);
		return notifications;
	}

	@Override
	public Long getMaxId() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "_id"));
		query.limit(1);
		
		Notification notification = mongoTemplate.findOne(query, Notification.class);
		return notification.getId();
	}

	@Override
	public Notification findOne(Long id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").gte(id));
		query.with(new Sort(Sort.Direction.ASC, "_id"));
		query.limit(1);
		
		Notification notification = mongoTemplate.findOne(query, Notification.class);
		if (id == notification.getId()) {
			return notification;
		}
		
		return null;
	}

}
