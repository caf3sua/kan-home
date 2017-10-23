package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.*;
import com.opisvn.kanhome.service.dto.TopicDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Topic and its DTO TopicDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper extends EntityMapper <TopicDTO, Topic> {
    
    

}
