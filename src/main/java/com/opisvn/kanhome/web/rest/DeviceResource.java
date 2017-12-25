package com.opisvn.kanhome.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.security.AuthoritiesConstants;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.DeviceService;
import com.opisvn.kanhome.service.DeviceStatService;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.dto.UserDeviceDTO;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.util.PaginationUtil;
import com.opisvn.kanhome.web.rest.vm.DeviceVM;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Device.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    private final DeviceService deviceService;
    
    private final DeviceStatService deviceStatService;
    
    private final UserService userService;

    public DeviceResource(DeviceService deviceService, UserService userService, DeviceStatService deviceStatService) {
        this.deviceService = deviceService;
        this.userService = userService;
        this.deviceStatService = deviceStatService;
    }

    /**
     * POST  /devices : Create a new device.
     *
     * @param deviceDTO the deviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deviceDTO, or with status 400 (Bad Request) if the device has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devices")
    @Timed
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to save Device : {}", deviceDTO);
        if (deviceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new device cannot already have an ID")).body(null);
        }
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity.created(new URI("/api/devices/" + result.getAbbrName()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getAbbrName().toString()))
            .body(result);
    }

    /**
     * PUT  /devices : Updates an existing device.
     *
     * @param deviceDTO the deviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deviceDTO,
     * or with status 400 (Bad Request) if the deviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the deviceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devices")
    @Timed
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to update Device : {}", deviceDTO);
        if (deviceDTO.getId() == null) {
            return createDevice(deviceDTO);
        }
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deviceDTO.getAbbrName().toString()))
            .body(result);
    }

    /**
     * GET  /devices : get all the devices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/devices")
    @Timed
    public ResponseEntity<List<DeviceDTO>> getAllDevices(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Devices");
        Page<DeviceDTO> page = deviceService.findAllWithGridView(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /devices/:id : get the "id" device.
     *
     * @param id the id of the deviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/devices/{id}")
    @Timed
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable String id) {
        log.debug("REST request to get Device : {}", id);
        DeviceDTO deviceDTO = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(deviceDTO));
    }
    
    @GetMapping("/get-by-abbrName/{abbrName}")
    @Timed
    public ResponseEntity<DeviceDTO> getDeviceByAbbrname(@PathVariable String abbrName) {
        log.debug("REST request to get getDeviceByAbbrname : {}", abbrName);
        DeviceDTO deviceDTO = deviceService.findOneByAbbrname(abbrName);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(deviceDTO));
    }

    /**
     * DELETE  /devices/:id : delete the "id" device.
     *
     * @param id the id of the deviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devices/{id}")
    @Timed
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        log.debug("REST request to delete Device : {}", id);
        DeviceDTO dto = deviceService.findOne(id);
        deviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, dto.getAbbrName())).build();
    }
    
    /**
     * POST  /delete-devices : delete the "id" devices.
     *
     * @param id the id of the deviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/delete-devices")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteDevices(@RequestBody String abbrNames) {
        log.debug("REST request to delete Devices : {}", abbrNames);
        if (StringUtils.isNotEmpty(abbrNames)) {
        	String[] abbrNamesArr = abbrNames.split(",");
        	for (String abbrName : abbrNamesArr) {
        		DeviceDTO dto = deviceService.findOneByAbbrname(abbrName);
        		if (dto != null && StringUtils.isNotEmpty(dto.getId())) {
        			deviceService.delete(dto.getId());
        		}
			}
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "kanHomeApp.device.deleteds", String.valueOf(abbrNames))).build();
    }
    
    /**
     * GET  /devices : get all the devices with simple data.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/devices-simple-data")
    @Timed
    public ResponseEntity<List<DeviceDTO>> getAllDevicesWithSimpleData() {
    	log.debug("REST request to get getAllDevicesWithSimpleData");
        List<DeviceDTO> lstDevice = deviceService.findAllWithSimpleData();
        return new ResponseEntity<>(lstDevice, null, HttpStatus.OK);
    }
    
    /**
     * POST  /devices-users : get all the devices-users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deviceStats in body
     */
    @PostMapping("/device-users")
    @Timed
    public ResponseEntity<List<DeviceDTO>> getAllUserByIds(@RequestBody List<DeviceDTO> devices) {
    	log.debug("REST request to getAllUserByIds, devices : {}", devices);
        
    	for (DeviceDTO deviceDTO : devices) {
    		List<UserDTO> lstUsers = deviceService.findUserByDeviceId(deviceDTO.getId());
    		if (lstUsers != null && lstUsers.size() > 0) {
    			deviceDTO.setUsers(lstUsers);
    		}
		}
        
        return new ResponseEntity<>(devices, null, HttpStatus.OK);
    }
    
    @GetMapping({"/devices_all_by_current_user", "/v1/device/all-by-current-user"})
    @Timed
    public ResponseEntity<List<UserDeviceDTO>> getAllDeviceByCurrentUser() {
    	log.debug("REST request to getAllDeviceByCurrentUser");
        
    	String username = SecurityUtils.getCurrentUserLogin();
    	
    	// User
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User not found")).body(null);
		}
		
		User user = userOpt.get();
        
        // Find device
        List<UserDeviceDTO> lstUserDevices = user.getUserDevices();
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO obj : lstUserDevices) {
        		DeviceDTO device = deviceService.findOne(obj.getId());
        		obj.setDevice(device);
    		}
    	}
        
        return new ResponseEntity<>(lstUserDevices, null, HttpStatus.OK);
    }
    
    @GetMapping("/devices-on-map")
    @Timed
    public ResponseEntity<List<DeviceDTO>> getAllDevicesOnMap() {
    	log.debug("REST request to getAllDevicesOnMap");
        List<DeviceDTO> lstDevice = deviceService.findAllWithMapView();
        return new ResponseEntity<>(lstDevice, null, HttpStatus.OK);
    }
    
    @PostMapping("/devices-search-on-grid")
    @Timed
    public ResponseEntity<List<DeviceDTO>> searchAllWithGridView(@RequestBody DeviceVM deviceVM) {
    	log.debug("REST request to searchAllWithGridView");
    	String[] sorts = deviceVM.getSort().get(0).split(",");
    	Sort sort = new Sort(Direction.fromString(sorts[1]), sorts[0]);
    	Pageable pageable = new PageRequest(deviceVM.getPage(), deviceVM.getSize(), sort);
    	
        Page<DeviceDTO> page = deviceService.searchAllWithGridView(pageable, deviceVM.getSearch());
        // Add status
        if (page != null && page.getContent() != null && page.getContent().size() != 0) {
        	for (DeviceDTO deviceDTO : page.getContent()) {
				// Get status
        		DeviceStatDTO stat = deviceStatService.findOne(deviceDTO.getId());
        		// More attribute
        		if (stat == null || StringUtils.isEmpty(stat.getDsts())) {
        			deviceDTO.setStatus("NO-STATUS");
        		} else {
        			deviceDTO.setStatus(stat.getDsts());
        		}
			}
        	
        	// Sort
        }
        
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devices-search-on-grid");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @PostMapping("/devices-search-on-map")
    @Timed
    public ResponseEntity<List<DeviceDTO>> searchAllDevicesOnMap(@RequestBody DeviceDTO deviceDTO) {
    	log.debug("REST request to searchAllDevicesOnMap");
        List<DeviceDTO> lstDevice = deviceService.searchAllWithMapView(deviceDTO);
        return new ResponseEntity<>(lstDevice, null, HttpStatus.OK);
    }
    
    @PostMapping("/devices-all-map")
    @Timed
    public ResponseEntity<List<DeviceDTO>> searchAllOnGridView(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Devices");
        Page<DeviceDTO> page = deviceService.findAllWithGridView(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
