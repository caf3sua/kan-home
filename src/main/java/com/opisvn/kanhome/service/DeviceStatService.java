package com.opisvn.kanhome.service;

import com.opisvn.kanhome.service.dto.DeviceStatDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DeviceStat.
 */
public interface DeviceStatService {

    /**
     * Save a deviceStat.
     *
     * @param deviceStatDTO the entity to save
     * @return the persisted entity
     */
    DeviceStatDTO save(DeviceStatDTO deviceStatDTO);

    /**
     *  Get all the deviceStats.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DeviceStatDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" deviceStat.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DeviceStatDTO findOne(String id);

    /**
     *  Delete the "id" deviceStat.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
    
    /**
     *  Get all the deviceStats.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    List<DeviceStatDTO> findByIds(List<DeviceStatDTO> deviceStats);
}
