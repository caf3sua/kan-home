package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.ModelDeviceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ModelDevice and its DTO ModelDeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModelDeviceMapper extends EntityMapper <ModelDeviceDTO, ModelDevice> {
    
    

}
