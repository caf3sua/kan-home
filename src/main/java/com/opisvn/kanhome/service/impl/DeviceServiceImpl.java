package com.opisvn.kanhome.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.repository.DeviceRepository;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.service.DeviceService;
import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.mapper.DeviceMapper;
import com.opisvn.kanhome.service.mapper.UserMapper;


/**
 * Service Implementation for managing Device.
 */
@Service
@CacheConfig(cacheNames = "device")
public class DeviceServiceImpl implements DeviceService{

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;
    
    private final UserRepository userRepository;

    private final DeviceMapper deviceMapper;
    
    private final UserMapper userMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper
    		, UserRepository userRepository, UserMapper userMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    /**
     *  Get all the devices.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<DeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable)
            .map(deviceMapper::toDto);
    }

    /**
     *  Get one device by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public DeviceDTO findOne(String id) {
        log.debug("Request to get Device : {}", id);
        Device device = deviceRepository.findOne(id);
        return deviceMapper.toDto(device);
    }

    /**
     *  Delete the  device by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.delete(id);
    }

	@Override
	@Cacheable
	public List<DeviceDTO> findAllWithSimpleData() {
		log.debug("Request to findAllWithSimpleData");
        List<Device> lstDevices = deviceRepository.findAllWithSimpleData();
        return deviceMapper.toDto(lstDevices);
	}

	@Override
	public Page<DeviceDTO> findAllWithGridView(Pageable pageable) {
		log.debug("Request to get all Devices for grid view");
        return deviceRepository.findAllWithGridView(pageable)
            .map(deviceMapper::toDto);
	}

	@Override
	public List<UserDTO> findUserByDeviceId(String deviceId) {
		log.debug("Request to findUserByDeviceId, deviceID : {}", deviceId);
        return userMapper.usersToUserDTOs(userRepository.findByDeviceId(deviceId));
	}

	@Override
	@Cacheable
	public List<DeviceDTO> findAllWithMapView() {
		log.debug("Request to findAllWithMapView");
        List<Device> lstDevices = deviceRepository.findAllWithMapView();
        return deviceMapper.toDto(lstDevices);
	}

	@Override
	public List<DeviceDTO> searchAllWithMapView(DeviceDTO deviceDTO) {
		log.debug("Request to searchAllWithMapView, deviceDTO {}", deviceDTO);
		List<Device> lstDevices = deviceRepository.searchAllWithMapView(deviceDTO);
		return deviceMapper.toDto(lstDevices);
	}

	@Override
	public Page<DeviceDTO> searchAllWithGridView(Pageable pageable, DeviceDTO deviceDTO) {
		log.debug("Request to get all Devices for grid view");
        return deviceRepository.searchAllWithGridView(pageable, deviceDTO)
            .map(deviceMapper::toDto);
	}

	@Override
	public DeviceDTO findOneByAbbrname(String abbrName) {
		List<Device> lstDevice = deviceRepository.findByAbbrName(abbrName);
		if (lstDevice == null || lstDevice.size() == 0) {
			return null;
		}
		
		return deviceMapper.toDto(lstDevice.get(0));
	}
}
