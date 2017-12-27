package com.opisvn.kanhome.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.service.dto.DeviceDTO;

//imports as static
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;


public class DeviceRepositoryImpl implements DeviceRepositoryExtend {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllDeviceType() {
		List<String> data = mongoTemplate.getCollection("device").distinct("name");
		return data;
	}

	private Criteria buildCriteria(DeviceDTO dto) {
		if (null == dto) {
			return null;
		}
		
		Criteria criteria = null;
		List<Criteria> criterias = new ArrayList<>();
		
		// Check abbrName (device type)
		if (StringUtils.isNotEmpty(dto.getAbbrName())) {
			Criteria criteriaModel = Criteria.where("abbrName").regex(dto.getAbbrName());
			criterias.add(criteriaModel);
		}
				
		// Check name (device type)
		if (StringUtils.isNotEmpty(dto.getModel())) {
			Criteria criteriaModel = Criteria.where("model").is(dto.getModel());
			criterias.add(criteriaModel);
		}
		
		// Check id
		if (StringUtils.isNotEmpty(dto.getId())) {
			Criteria criteriaId = Criteria.where("_id").is(dto.getId());
			criterias.add(criteriaId);
		}
		
		// Countrycode
		if (dto.getCountryCode() != null && dto.getCountryCode() > 0) {
			Criteria criteriaCountry = Criteria.where("countryCode").is(dto.getCountryCode());
			criterias.add(criteriaCountry);
		}
		
		// Citycode
		if (dto.getCityCode() != null && dto.getCityCode() > 0) {
			Criteria criteriaCty = Criteria.where("ctyCode").is(dto.getCityCode());
			criterias.add(criteriaCty);
		}
		
		// District code
		if (dto.getDistCode() != null && dto.getDistCode() > 0) {
			Criteria criteriaDistrict = Criteria.where("distCode").is(dto.getDistCode());
			criterias.add(criteriaDistrict);
		}
		
		if (criterias.size() > 0) {
			// Join
			criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		}
		
		return criteria;
	}
	
	@Override
	public List<Device> search(DeviceDTO dto) {
		Criteria criteria = buildCriteria(dto);
		
		List<Device> devices = null;
		
		if (criteria != null) {
			// Search
			Query query = new Query();
			query.addCriteria(criteria);
	
			devices = mongoTemplate.find(query, Device.class);
		} else {
			devices = mongoTemplate.findAll(Device.class);
		}
		return devices;
	}

	@Override
	public List<Device> findAllWithSimpleData() {
		Query query = new Query();
		query.fields().include("_id");
		query.fields().include("abbrName");

		return mongoTemplate.find(query, Device.class);
	}

	@Override
	public List<Device> findAllWithMapView() {
		return searchAllWithMapView(null);
	}
	
	@Override
	public Page<Device> findAllWithGridView(Pageable pageable) {
		return searchAllWithGridView(pageable, null);
	}

	@Override
	public List<Device> searchAllWithMapView(DeviceDTO dto) {
		Criteria criteria = buildCriteria(dto);

		Aggregation agg = null;
		// Match
		if (criteria != null) {
			agg = newAggregation(
					lookup("deviceStat", "_id", "_id", "status"),
					unwind("status", true),
					project("_id", "abbrName", "gpsX", "gpsY").andExpression("status.dsts").as("dsts"),
					match(criteria)
				);
		} else {
			agg = newAggregation(
					lookup("deviceStat", "_id", "_id", "status"),
					unwind("status", true),
					project("_id", "abbrName", "gpsX", "gpsY").andExpression("status.dsts").as("dsts")
				);
		}
		
		//Convert the aggregation result into a List
		AggregationResults<Device> groupResults
			= mongoTemplate.aggregate(agg, "device", Device.class);
		List<Device> devices = groupResults.getMappedResults();

		return devices;
	}

	@Override
	public Page<Device> searchAllWithGridView(Pageable pageable, DeviceDTO dto) {
		Criteria criteria = buildCriteria(dto);
		
		// Calculate total
		Query query = new Query();
		query.with(pageable);
		query.fields().include("_id");
//		query.fields().include("model");
//		query.fields().include("abbrName");
//		query.fields().include("countryCode");
					
		// aggregate
		Aggregation agg = null;
		// Match
		if (criteria != null) {
			agg = newAggregation(
					lookup("deviceStat", "_id", "_id", "status"),
					match(criteria),
					unwind("status", true),
					project("_id", "model", "abbrName", "countryCode").andExpression("status.dsts").as("dsts").andExpression("_id").as("id"),
					sort(pageable.getSort()),
					skip((long) (pageable.getPageNumber() * pageable.getPageSize())),
					limit(pageable.getPageSize())					
				);
			// Search
			query.addCriteria(criteria);
		} else {
			agg = newAggregation(
					lookup("deviceStat", "_id", "_id", "status"),
					unwind("status", true),
					project("_id", "model", "abbrName", "countryCode").andExpression("status.dsts").as("dsts").andExpression("_id").as("id"),
					sort(pageable.getSort()),
					skip((long) (pageable.getPageNumber() * pageable.getPageSize())),
					limit(pageable.getPageSize())					
				);
		}
		
		long total = mongoTemplate.count(query, Device.class);
		
		//Convert the aggregation result into a List
		AggregationResults<Device> groupResults
			= mongoTemplate.aggregate(agg, "device", Device.class);
		List<Device> devices = groupResults.getMappedResults();

		
		// Build pageable
		Page<Device> devicePage = new PageImpl<Device>(devices, pageable, total);
		return devicePage;
	}

//	db.device.aggregate([{
//    $lookup: {
//            from: "deviceStat",
//            localField: "_id",
//            foreignField: "_id",
//            as: "status"
//        }
//	},
//  {
//          $match: { "status": { $elemMatch: { dsts: 'BLUE' } } }
//  }
//]);
	
//	db.device.aggregate([{
//	    $lookup: {
//	            from: "deviceStat",
//	            localField: "_id",
//	            foreignField: "_id",
//	            as: "status"
//	        }
//		},
//		{   
//	        $unwind: { path: "$status", preserveNullAndEmptyArrays: true }
//	    },
//		{ 
//		    $project : { "_id" : 1, "abbrName" : 1, "gpsX": 1, "gpsY" : 1, "dsts": "$status.dsts"}
//		},
//	    {
//	         $match: {"dsts": "BLUE"} 
//	    },
//	]);
}
