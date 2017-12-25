package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.ModelDeviceService;
import com.opisvn.kanhome.domain.ModelDevice;
import com.opisvn.kanhome.repository.ModelDeviceRepository;
import com.opisvn.kanhome.service.dto.ModelDeviceDTO;
import com.opisvn.kanhome.service.mapper.ModelDeviceMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing ModelDevice.
 */
@Service
@CacheConfig(cacheNames = "model")
public class ModelDeviceServiceImpl implements ModelDeviceService{

    private final Logger log = LoggerFactory.getLogger(ModelDeviceServiceImpl.class);

    private final ModelDeviceRepository modelDeviceRepository;

    private final ModelDeviceMapper modelDeviceMapper;

    public ModelDeviceServiceImpl(ModelDeviceRepository modelDeviceRepository, ModelDeviceMapper modelDeviceMapper) {
        this.modelDeviceRepository = modelDeviceRepository;
        this.modelDeviceMapper = modelDeviceMapper;
    }

    /**
     * Save a modelDevice.
     *
     * @param modelDeviceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ModelDeviceDTO save(ModelDeviceDTO modelDeviceDTO) {
        log.debug("Request to save ModelDevice : {}", modelDeviceDTO);
        ModelDevice modelDevice = modelDeviceMapper.toEntity(modelDeviceDTO);
        modelDevice = modelDeviceRepository.save(modelDevice);
        return modelDeviceMapper.toDto(modelDevice);
    }

    /**
     *  Get all the modelDevices.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Cacheable
    public Page<ModelDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ModelDevices");
        return modelDeviceRepository.findAll(pageable)
            .map(modelDeviceMapper::toDto);
    }

    /**
     *  Get one modelDevice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public ModelDeviceDTO findOne(String id) {
        log.debug("Request to get ModelDevice : {}", id);
        ModelDevice modelDevice = modelDeviceRepository.findOne(id);
        return modelDeviceMapper.toDto(modelDevice);
    }

    /**
     *  Delete the  modelDevice by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ModelDevice : {}", id);
        modelDeviceRepository.delete(id);
    }

	@Override
	@Cacheable
	public List<ModelDeviceDTO> findAll() {
		log.debug("Request to findAll");
        return modelDeviceMapper.toDto(modelDeviceRepository.findAll());
	}

	@Override
	public ModelDeviceDTO findOneByModel(String model) {
		log.debug("Request to get ModelDevice : {}", model);
        ModelDevice modelDevice = modelDeviceRepository.findOneByModel(model);
        return modelDeviceMapper.toDto(modelDevice);
	}
}
