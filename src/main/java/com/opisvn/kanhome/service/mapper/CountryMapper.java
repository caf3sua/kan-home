package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.CountryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper extends EntityMapper <CountryDTO, Country> {
    
    

}
