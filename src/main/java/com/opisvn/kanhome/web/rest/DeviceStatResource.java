package com.opisvn.kanhome.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.service.DeviceStatService;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.util.PaginationUtil;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DeviceStat.
 */
@RestController
@RequestMapping("/api")
public class DeviceStatResource {

    private final Logger log = LoggerFactory.getLogger(DeviceStatResource.class);

    private static final String ENTITY_NAME = "deviceStat";

    private final DeviceStatService deviceStatService;

    public DeviceStatResource(DeviceStatService deviceStatService) {
        this.deviceStatService = deviceStatService;
    }

    /**
     * POST  /device-stats : Create a new deviceStat.
     *
     * @param deviceStatDTO the deviceStatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deviceStatDTO, or with status 400 (Bad Request) if the deviceStat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/device-stats")
    @Timed
    public ResponseEntity<DeviceStatDTO> createDeviceStat(@RequestBody DeviceStatDTO deviceStatDTO) throws URISyntaxException {
        log.debug("REST request to save DeviceStat : {}", deviceStatDTO);
        if (deviceStatDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new deviceStat cannot already have an ID")).body(null);
        }
        DeviceStatDTO result = deviceStatService.save(deviceStatDTO);
        return ResponseEntity.created(new URI("/api/device-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /device-stats : Updates an existing deviceStat.
     *
     * @param deviceStatDTO the deviceStatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deviceStatDTO,
     * or with status 400 (Bad Request) if the deviceStatDTO is not valid,
     * or with status 500 (Internal Server Error) if the deviceStatDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/device-stats")
    @Timed
    public ResponseEntity<DeviceStatDTO> updateDeviceStat(@RequestBody DeviceStatDTO deviceStatDTO) throws URISyntaxException {
        log.debug("REST request to update DeviceStat : {}", deviceStatDTO);
        if (deviceStatDTO.getId() == null) {
            return createDeviceStat(deviceStatDTO);
        }
        DeviceStatDTO result = deviceStatService.save(deviceStatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deviceStatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /device-stats : get all the deviceStats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deviceStats in body
     */
    @GetMapping("/device-stats")
    @Timed
    public ResponseEntity<List<DeviceStatDTO>> getAllDeviceStats(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DeviceStats");
        Page<DeviceStatDTO> page = deviceStatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/device-stats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /device-stats/:id : get the "id" deviceStat.
     *
     * @param id the id of the deviceStatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deviceStatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/device-stats/{id}")
    @Timed
    public ResponseEntity<DeviceStatDTO> getDeviceStat(@PathVariable String id) {
        log.debug("REST request to get DeviceStat : {}", id);
        DeviceStatDTO deviceStatDTO = deviceStatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(deviceStatDTO));
    }

    /**
     * DELETE  /device-stats/:id : delete the "id" deviceStat.
     *
     * @param id the id of the deviceStatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/device-stats/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeviceStat(@PathVariable String id) {
        log.debug("REST request to delete DeviceStat : {}", id);
        deviceStatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
    
    /**
     * POST  /device-stats-by-ids : get all the deviceStats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deviceStats in body
     */
    @PostMapping("/device-stats-by-ids")
    @Timed
    public ResponseEntity<List<DeviceStatDTO>> getAllDeviceStatsByIds(@RequestBody List<DeviceStatDTO> deviceStats) {
    	log.debug("REST request to getAllDeviceStatsByIds");
        List<DeviceStatDTO> lstDeviceStats = deviceStatService.findByIds(deviceStats);
        return new ResponseEntity<>(lstDeviceStats, null, HttpStatus.OK);
    }
}
