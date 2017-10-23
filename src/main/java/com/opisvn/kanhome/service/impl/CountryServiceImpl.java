package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.CountryService;
import com.opisvn.kanhome.domain.Country;
import com.opisvn.kanhome.repository.CountryRepository;
import com.opisvn.kanhome.service.dto.CountryDTO;
import com.opisvn.kanhome.service.mapper.CountryMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing Country.
 */
@Service
public class CountryServiceImpl implements CountryService{

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    /**
     *  Get all the countries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAll(pageable)
            .map(countryMapper::toDto);
    }

    /**
     *  Get one country by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public CountryDTO findOne(String id) {
        log.debug("Request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        return countryMapper.toDto(country);
    }

    /**
     *  Delete the  country by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.delete(id);
    }

	@Override
	public List<CountryDTO> findAll() {
		return countryMapper.toDto(countryRepository.findAll());
	}

	@Override
	public CountryDTO findOneByCountryCode(Long countryCode) {
		log.debug("Request to get Country : {}", countryCode);
        Country country = countryRepository.findOneByCountryCode(String.valueOf(countryCode));
        return countryMapper.toDto(country);
	}
}
