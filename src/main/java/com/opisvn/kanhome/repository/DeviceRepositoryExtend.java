package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.service.dto.DeviceDTO;


@Repository
public interface DeviceRepositoryExtend {

	List<String> getAllDeviceType();
	
	List<Device> search(DeviceDTO dto);
	
	List<Device> findAllWithSimpleData();
	
	List<Device> findAllWithMapView();
	
	List<Device> searchAllWithMapView(DeviceDTO dto);
	
	Page<Device> findAllWithGridView(Pageable pageable);
	
	Page<Device> searchAllWithGridView(Pageable pageable, DeviceDTO dto);
}
