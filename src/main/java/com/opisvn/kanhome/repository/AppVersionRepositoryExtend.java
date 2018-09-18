package com.opisvn.kanhome.repository;

import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.AppVersion;



@Repository
public interface AppVersionRepositoryExtend {

	AppVersion findLatestOne();
}
