package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.CityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity City and its DTO CityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CityMapper extends EntityMapper <CityDTO, City> {
    
    

}
