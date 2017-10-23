package com.opisvn.kanhome.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.UserDTO;

public class UserRepositoryImpl implements UserRepositoryExtend {
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public List<User> findByDeviceId(String deviceId) {
		// db.getCollection('user').find({userDevices: { $elemMatch: {'_id': '6kfC0000'}}})
		BasicQuery query = new BasicQuery("{userDevices: { $elemMatch: { '_id': '" + deviceId + "'}}}");
		
		query.fields().include("_id");
		query.fields().include("username");

		List<User> data = mongoTemplate.find(query, User.class);
		return data;
	}
	
	
	private Criteria buildCriteria(UserDTO dto) {
		Criteria criteria = null;
		List<Criteria> criterias = new ArrayList<>();
		
		// Check username
		if (StringUtils.isNotEmpty(dto.getUsername())) {
			//Criteria criteriaModel = Criteria.where("username").is(dto.getUsername());
			Criteria criteriaModel = Criteria.where("username").regex(dto.getUsername());
			criterias.add(criteriaModel);
		}
				
		// Check email
		if (StringUtils.isNotEmpty(dto.getEmail())) {
			Criteria criteriaModel = Criteria.where("email").is(dto.getEmail());
			criterias.add(criteriaModel);
		}
		
		// Check email
		if (StringUtils.isNotEmpty(dto.getPhonenumber())) {
			Criteria criteriaModel = Criteria.where("phonenumber").regex(dto.getPhonenumber());
			criterias.add(criteriaModel);
		}
				
		// Check role
		if (dto.getAuthorities() != null && dto.getAuthorities().size() > 0) {
			List<Criteria> lstAuthCriteria = new ArrayList<>();
			Set<String> authorities = dto.getAuthorities();
			for (String auth : authorities) {
				Criteria criteriaAuth = Criteria.where("authorities").elemMatch(Criteria.where("_id").is(auth));
				lstAuthCriteria.add(criteriaAuth);
			}
			

			if (lstAuthCriteria.size() > 0) {
				Criteria criteriaAllAuth = new Criteria().orOperator(lstAuthCriteria.toArray(new Criteria[lstAuthCriteria.size()]));
				criterias.add(criteriaAllAuth);
			}
		}
		
		if (criterias.size() > 0) {
			// Join
			criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		}
		
		return criteria;
	}

	@Override
	public Page<User> search(Pageable pageable, UserDTO dto) {
		Criteria criteria = buildCriteria(dto);
		// Search
		Query query = new Query();
		query.with(pageable);
					
		List<User> devices = null;
		
		if (criteria != null) {
			// Search
			query.addCriteria(criteria);
	
			devices = mongoTemplate.find(query, User.class);
		} else {
			devices = mongoTemplate.find(query, User.class);
		}
		
		long total = mongoTemplate.count(query, User.class);
		Page<User> devicePage = new PageImpl<User>(devices, pageable, total);
		return devicePage;
	}

	@Override
	public Long countByRole(String role) {
		Criteria criteriaAuth = Criteria.where("authorities").elemMatch(Criteria.where("_id").is(role));
		Query query = new Query();
		query.addCriteria(criteriaAuth);
		
		return mongoTemplate.count(query, User.class);
	}
}
