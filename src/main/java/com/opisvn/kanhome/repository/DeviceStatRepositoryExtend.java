package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.DeviceStat;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;


@Repository
public interface DeviceStatRepositoryExtend {

	List<DeviceStat> findByIds(List<DeviceStatDTO> deviceStats);
}
