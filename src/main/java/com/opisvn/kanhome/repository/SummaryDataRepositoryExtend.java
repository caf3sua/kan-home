package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.Notification;
import com.opisvn.kanhome.domain.SummaryData;



@Repository
public interface SummaryDataRepositoryExtend {

	SummaryData findLatestOne();
}
