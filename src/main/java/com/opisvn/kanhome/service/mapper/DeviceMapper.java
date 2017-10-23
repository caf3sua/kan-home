package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.DeviceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Device and its DTO DeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceMapper extends EntityMapper <DeviceDTO, Device> {
    
    

}
