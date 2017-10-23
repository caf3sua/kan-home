package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.ModelDevice;
import com.opisvn.kanhome.service.dto.FilterDTO;
import com.opisvn.kanhome.service.dto.ModelDeviceDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-19T22:30:24+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class ModelDeviceMapperImpl implements ModelDeviceMapper {

    @Override
    public ModelDevice toEntity(ModelDeviceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ModelDevice modelDevice = new ModelDevice();

        Map<Integer, FilterDTO> map = dto.getFilters();
        if ( map != null ) {
            modelDevice.setFilters(       new HashMap<Integer, FilterDTO>( map )
            );
        }
        modelDevice.setId( dto.getId() );
        modelDevice.setName( dto.getName() );
        modelDevice.setModel( dto.getModel() );
        modelDevice.setWaranty( dto.getWaranty() );
        modelDevice.setImageUrl( dto.getImageUrl() );

        return modelDevice;
    }

    @Override
    public ModelDeviceDTO toDto(ModelDevice entity) {
        if ( entity == null ) {
            return null;
        }

        ModelDeviceDTO modelDeviceDTO = new ModelDeviceDTO();

        modelDeviceDTO.setId( entity.getId() );
        modelDeviceDTO.setName( entity.getName() );
        modelDeviceDTO.setModel( entity.getModel() );
        modelDeviceDTO.setWaranty( entity.getWaranty() );
        modelDeviceDTO.setImageUrl( entity.getImageUrl() );
        Map<Integer, FilterDTO> map = entity.getFilters();
        if ( map != null ) {
            modelDeviceDTO.setFilters(       new HashMap<Integer, FilterDTO>( map )
            );
        }

        return modelDeviceDTO;
    }

    @Override
    public List<ModelDevice> toEntity(List<ModelDeviceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ModelDevice> list = new ArrayList<ModelDevice>();
        for ( ModelDeviceDTO modelDeviceDTO : dtoList ) {
            list.add( toEntity( modelDeviceDTO ) );
        }

        return list;
    }

    @Override
    public List<ModelDeviceDTO> toDto(List<ModelDevice> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ModelDeviceDTO> list = new ArrayList<ModelDeviceDTO>();
        for ( ModelDevice modelDevice : entityList ) {
            list.add( toDto( modelDevice ) );
        }

        return list;
    }
}
