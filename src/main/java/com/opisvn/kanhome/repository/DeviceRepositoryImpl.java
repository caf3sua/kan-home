package com.opisvn.kanhome.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.service.dto.DeviceDTO;

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
		Query query = new Query();
		query.fields().include("_id");
		query.fields().include("abbrName");
		query.fields().include("gpsX");
		query.fields().include("gpsY");

		return mongoTemplate.find(query, Device.class);
	}
	
	@Override
	public Page<Device> findAllWithGridView(Pageable pageable) {
		Query query = new Query();
		query.with(pageable);
		query.fields().include("_id");
		query.fields().include("model");
		query.fields().include("abbrName");
		query.fields().include("countryCode");
		
		List<Device> lstDevices = mongoTemplate.find(query, Device.class);
		long total = mongoTemplate.count(query, Device.class);
		Page<Device> devicePage = new PageImpl<Device>(lstDevices, pageable, total);

		return devicePage;
	}

	@Override
	public List<Device> searchAllWithMapView(DeviceDTO dto) {
		Criteria criteria = buildCriteria(dto);
		// Search
		Query query = new Query();
		query.fields().include("_id");
		query.fields().include("abbrName");
		query.fields().include("gpsX");
		query.fields().include("gpsY");
					
		List<Device> devices = null;
		
		if (criteria != null) {
			// Search
			query.addCriteria(criteria);
	
			devices = mongoTemplate.find(query, Device.class);
		} else {
			devices = mongoTemplate.find(query, Device.class);
		}
		return devices;
	}

	@Override
	public Page<Device> searchAllWithGridView(Pageable pageable, DeviceDTO dto) {
		Criteria criteria = buildCriteria(dto);
		// Search
		Query query = new Query();
		query.with(pageable);
		query.fields().include("_id");
		query.fields().include("model");
		query.fields().include("abbrName");
		query.fields().include("countryCode");
					
		List<Device> devices = null;
		
		if (criteria != null) {
			// Search
			query.addCriteria(criteria);
	
			devices = mongoTemplate.find(query, Device.class);
		} else {
			devices = mongoTemplate.find(query, Device.class);
		}
		
		long total = mongoTemplate.count(query, Device.class);
		Page<Device> devicePage = new PageImpl<Device>(devices, pageable, total);
		return devicePage;
	}

}
