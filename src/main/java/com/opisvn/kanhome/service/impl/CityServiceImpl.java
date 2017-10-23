package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.CityService;
import com.opisvn.kanhome.domain.City;
import com.opisvn.kanhome.repository.CityRepository;
import com.opisvn.kanhome.service.dto.CityDTO;
import com.opisvn.kanhome.service.mapper.CityMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing City.
 */
@Service
public class CityServiceImpl implements CityService{

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        City city = cityMapper.toEntity(cityDTO);
        city = cityRepository.save(city);
        return cityMapper.toDto(city);
    }

    /**
     *  Get all the cities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<CityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return cityRepository.findAll(pageable)
            .map(cityMapper::toDto);
    }

    /**
     *  Get one city by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public CityDTO findOne(String id) {
        log.debug("Request to get City : {}", id);
        City city = cityRepository.findOne(id);
        return cityMapper.toDto(city);
    }

    /**
     *  Delete the  city by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete City : {}", id);
        cityRepository.delete(id);
    }

	@Override
	public List<CityDTO> findAll() {
		log.debug("Request to findAll");
		return cityMapper.toDto(cityRepository.findAll());
	}
}
