package com.opisvn.kanhome.repository;

import com.opisvn.kanhome.domain.Country;
import com.opisvn.kanhome.domain.Device;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends MongoRepository<Device,String>, DeviceRepositoryExtend {
	
	List<Device> findByAbbrName(String abbrName);
}
