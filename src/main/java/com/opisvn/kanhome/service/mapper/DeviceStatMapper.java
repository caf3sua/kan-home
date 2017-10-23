package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DeviceStat and its DTO DeviceStatDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceStatMapper extends EntityMapper <DeviceStatDTO, DeviceStat> {
    
    

}
