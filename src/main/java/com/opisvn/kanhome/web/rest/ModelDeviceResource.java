package com.opisvn.kanhome.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.service.ModelDeviceService;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.util.PaginationUtil;
import com.opisvn.kanhome.service.dto.ModelDeviceDTO;
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
 * REST controller for managing ModelDevice.
 */
@RestController
@RequestMapping("/api")
public class ModelDeviceResource {

    private final Logger log = LoggerFactory.getLogger(ModelDeviceResource.class);

    private static final String ENTITY_NAME = "modelDevice";

    private final ModelDeviceService modelDeviceService;

    public ModelDeviceResource(ModelDeviceService modelDeviceService) {
        this.modelDeviceService = modelDeviceService;
    }

    /**
     * POST  /model-devices : Create a new modelDevice.
     *
     * @param modelDeviceDTO the modelDeviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modelDeviceDTO, or with status 400 (Bad Request) if the modelDevice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/model-devices")
    @Timed
    public ResponseEntity<ModelDeviceDTO> createModelDevice(@RequestBody ModelDeviceDTO modelDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save ModelDevice : {}", modelDeviceDTO);
        if (modelDeviceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new modelDevice cannot already have an ID")).body(null);
        }
        ModelDeviceDTO result = modelDeviceService.save(modelDeviceDTO);
        return ResponseEntity.created(new URI("/api/model-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /model-devices : Updates an existing modelDevice.
     *
     * @param modelDeviceDTO the modelDeviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modelDeviceDTO,
     * or with status 400 (Bad Request) if the modelDeviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the modelDeviceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/model-devices")
    @Timed
    public ResponseEntity<ModelDeviceDTO> updateModelDevice(@RequestBody ModelDeviceDTO modelDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update ModelDevice : {}", modelDeviceDTO);
        if (modelDeviceDTO.getId() == null) {
            return createModelDevice(modelDeviceDTO);
        }
        ModelDeviceDTO result = modelDeviceService.save(modelDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modelDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /model-devices : get all the modelDevices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of modelDevices in body
     */
    @GetMapping("/model-devices")
    @Timed
    public ResponseEntity<List<ModelDeviceDTO>> getAllModelDevices(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ModelDevices");
        Page<ModelDeviceDTO> page = modelDeviceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/model-devices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /model-devices/:id : get the "id" modelDevice.
     *
     * @param id the id of the modelDeviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modelDeviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/model-devices/{id}")
    @Timed
    public ResponseEntity<ModelDeviceDTO> getModelDevice(@PathVariable String id) {
        log.debug("REST request to get ModelDevice : {}", id);
        ModelDeviceDTO modelDeviceDTO = modelDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modelDeviceDTO));
    }

    /**
     * DELETE  /model-devices/:id : delete the "id" modelDevice.
     *
     * @param id the id of the modelDeviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/model-devices/{id}")
    @Timed
    public ResponseEntity<Void> deleteModelDevice(@PathVariable String id) {
        log.debug("REST request to delete ModelDevice : {}", id);
        modelDeviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
    
    @GetMapping({"/model-devices-all", "/v1/model/all"})
    @Timed
    public ResponseEntity<List<ModelDeviceDTO>> getAllModelDevicesOnMap() {
        log.debug("REST request to get a page of ModelDevices");
        List<ModelDeviceDTO> lst = modelDeviceService.findAll();
        return new ResponseEntity<>(lst, null, HttpStatus.OK);
    }
    
    @GetMapping("/model-devices-by-model/{model}")
    @Timed
    public ResponseEntity<ModelDeviceDTO> getModelDeviceByModel(@PathVariable String model) {
        log.debug("REST request to get getModelDeviceByModel : {}", model);
        ModelDeviceDTO modelDeviceDTO = modelDeviceService.findOneByModel(model);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modelDeviceDTO));
    }
}
