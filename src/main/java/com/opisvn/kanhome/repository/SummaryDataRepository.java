package com.opisvn.kanhome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.SummaryData;

/**
 * Spring Data MongoDB repository for the Topic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SummaryDataRepository extends MongoRepository<SummaryData,String>, SummaryDataRepositoryExtend {

}
