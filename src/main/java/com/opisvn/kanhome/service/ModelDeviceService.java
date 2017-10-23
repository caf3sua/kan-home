package com.opisvn.kanhome.service;

import com.opisvn.kanhome.service.dto.ModelDeviceDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ModelDevice.
 */
public interface ModelDeviceService {

    /**
     * Save a modelDevice.
     *
     * @param modelDeviceDTO the entity to save
     * @return the persisted entity
     */
    ModelDeviceDTO save(ModelDeviceDTO modelDeviceDTO);

    /**
     *  Get all the modelDevices.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ModelDeviceDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" modelDevice.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ModelDeviceDTO findOne(String id);

    /**
     *  Delete the "id" modelDevice.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
    
    List<ModelDeviceDTO> findAll();
    
    ModelDeviceDTO findOneByModel(String model);
}
