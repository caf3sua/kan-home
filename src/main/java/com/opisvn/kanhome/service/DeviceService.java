package com.opisvn.kanhome.service;

import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.UserDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Device.
 */
public interface DeviceService {

    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save
     * @return the persisted entity
     */
    DeviceDTO save(DeviceDTO deviceDTO);

    /**
     *  Get all the devices.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DeviceDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" device.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DeviceDTO findOne(String id);
    
    DeviceDTO findOneByAbbrname(String abbrName);

    /**
     *  Delete the "id" device.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
    
    List<DeviceDTO> findAllWithSimpleData();
    
    List<DeviceDTO> findAllWithMapView();
    
    List<DeviceDTO> searchAllWithMapView(DeviceDTO deviceDTO);
    
    Page<DeviceDTO> findAllWithGridView(Pageable pageable);
    
    List<UserDTO> findUserByDeviceId(String deviceId);
    
    Page<DeviceDTO> searchAllWithGridView(Pageable pageable, DeviceDTO deviceDTO);
}

