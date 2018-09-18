package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.AppVersion;
import com.opisvn.kanhome.domain.SummaryData;

public class AppVersionRepositoryImpl implements AppVersionRepositoryExtend {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public AppVersion findLatestOne() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "_id"));
		query.limit(1);
		
		List<AppVersion> data = mongoTemplate.find(query, AppVersion.class);
		if (data != null && data.size() > 0) {
			return data.get(0);
		}
		
		return null;
	}


}
