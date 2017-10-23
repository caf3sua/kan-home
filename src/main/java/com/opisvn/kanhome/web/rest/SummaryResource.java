package com.opisvn.kanhome.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.domain.SummaryData;
import com.opisvn.kanhome.repository.SummaryDataRepository;

/**
 * REST controller for managing Topic.
 */
@RestController
@RequestMapping("/api")
public class SummaryResource {

    private final Logger log = LoggerFactory.getLogger(SummaryResource.class);

    private static final String ENTITY_NAME = "summary";

    private final SummaryDataRepository summaryDataRepository;

    public SummaryResource(SummaryDataRepository summaryDataRepository) {
        this.summaryDataRepository = summaryDataRepository;
    }

    /**
     * GET  /topics : get all the topics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of topics in body
     */
    @GetMapping("/summary-data")
    @Timed
    public ResponseEntity<SummaryData> getLatestSummary() {
        log.debug("REST request to getLatestSummary");
        SummaryData summaryData = summaryDataRepository.findLatestOne();
        return new ResponseEntity<>(summaryData, null, HttpStatus.OK);
    }
}
