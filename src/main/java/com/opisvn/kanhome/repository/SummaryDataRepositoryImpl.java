package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.SummaryData;

public class SummaryDataRepositoryImpl implements SummaryDataRepositoryExtend {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public SummaryData findLatestOne() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "_id"));
		query.limit(1);
		
		List<SummaryData> summaryData = mongoTemplate.find(query, SummaryData.class);
		if (summaryData != null && summaryData.size() > 0) {
			return summaryData.get(0);
		}
		
		return null;
	}


}
