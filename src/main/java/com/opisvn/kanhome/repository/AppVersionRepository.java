package com.opisvn.kanhome.repository;

import com.opisvn.kanhome.domain.ActiveCodes;
import com.opisvn.kanhome.domain.AppVersion;
import com.opisvn.kanhome.domain.Country;
import com.opisvn.kanhome.domain.Topic;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ActiveCodes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppVersionRepository extends MongoRepository<AppVersion,String>, AppVersionRepositoryExtend  {
}
