package com.opisvn.kanhome.repository;

import com.opisvn.kanhome.domain.ModelDevice;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ModelDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelDeviceRepository extends MongoRepository<ModelDevice,String> {
	ModelDevice findOneByModel(String model);
}
