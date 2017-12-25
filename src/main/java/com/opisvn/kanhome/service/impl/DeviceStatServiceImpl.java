package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.DeviceStatService;
import com.opisvn.kanhome.domain.DeviceStat;
import com.opisvn.kanhome.repository.DeviceStatRepository;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;
import com.opisvn.kanhome.service.mapper.DeviceStatMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing DeviceStat.
 */
@Service
@CacheConfig(cacheNames = "deviceStat")
public class DeviceStatServiceImpl implements DeviceStatService{

    private final Logger log = LoggerFactory.getLogger(DeviceStatServiceImpl.class);

    private final DeviceStatRepository deviceStatRepository;

    private final DeviceStatMapper deviceStatMapper;

    public DeviceStatServiceImpl(DeviceStatRepository deviceStatRepository, DeviceStatMapper deviceStatMapper) {
        this.deviceStatRepository = deviceStatRepository;
        this.deviceStatMapper = deviceStatMapper;
    }

    /**
     * Save a deviceStat.
     *
     * @param deviceStatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DeviceStatDTO save(DeviceStatDTO deviceStatDTO) {
        log.debug("Request to save DeviceStat : {}", deviceStatDTO);
        DeviceStat deviceStat = deviceStatMapper.toEntity(deviceStatDTO);
        deviceStat = deviceStatRepository.save(deviceStat);
        return deviceStatMapper.toDto(deviceStat);
    }

    /**
     *  Get all the deviceStats.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<DeviceStatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceStats");
        return deviceStatRepository.findAll(pageable)
            .map(deviceStatMapper::toDto);
    }

    /**
     *  Get one deviceStat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public DeviceStatDTO findOne(String id) {
        log.debug("Request to get DeviceStat : {}", id);
        DeviceStat deviceStat = deviceStatRepository.findOne(id);
        return deviceStatMapper.toDto(deviceStat);
    }

    /**
     *  Delete the  deviceStat by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete DeviceStat : {}", id);
        deviceStatRepository.delete(id);
    }

	@Override
	@Cacheable
	public List<DeviceStatDTO> findByIds(List<DeviceStatDTO> deviceStats) {
		log.debug("Request to findByIds : {}", deviceStats);
        return deviceStatMapper.toDto(deviceStatRepository.findByIds(deviceStats));
	}
}
