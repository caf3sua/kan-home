package com.opisvn.kanhome.repository;

import com.opisvn.kanhome.domain.DeviceStat;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the DeviceStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceStatRepository extends MongoRepository<DeviceStat,String>, DeviceStatRepositoryExtend {

}
