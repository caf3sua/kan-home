package com.opisvn.kanhome.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.DeviceStat;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;

public class DeviceStatRepositoryImpl implements DeviceStatRepositoryExtend {
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public List<DeviceStat> findByIds(List<DeviceStatDTO> deviceStatDTOs) {
		List<String> listOfIds = new ArrayList<>();
		for (DeviceStatDTO stat : deviceStatDTOs) {
			listOfIds.add(stat.getId());
		}

		Query query = new Query();
		query.fields().include("id");
		query.fields().include("dsts");
		
		query.addCriteria(Criteria.where("_id").in(listOfIds));

		List<DeviceStat> deviceStats = mongoTemplate.find(query, DeviceStat.class);
		return deviceStats;
	}
	
	

}
