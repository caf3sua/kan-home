package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.DeviceStat;
import com.opisvn.kanhome.service.dto.DeviceStatDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-11-27T23:13:07+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class DeviceStatMapperImpl implements DeviceStatMapper {

    @Override
    public DeviceStat toEntity(DeviceStatDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DeviceStat deviceStat = new DeviceStat();

        deviceStat.setId( dto.getId() );
        deviceStat.setType( dto.getType() );
        deviceStat.setClientid( dto.getClientid() );
        deviceStat.setTime( dto.getTime() );
        deviceStat.setU( dto.getU() );
        deviceStat.setwQ( dto.getwQ() );

        return deviceStat;
    }

    @Override
    public DeviceStatDTO toDto(DeviceStat entity) {
        if ( entity == null ) {
            return null;
        }

        DeviceStatDTO deviceStatDTO = new DeviceStatDTO();

        deviceStatDTO.setId( entity.getId() );
        deviceStatDTO.setType( entity.getType() );
        deviceStatDTO.setClientid( entity.getClientid() );
        deviceStatDTO.setTime( entity.getTime() );
        deviceStatDTO.setU( entity.getU() );
        deviceStatDTO.setwQ( entity.getwQ() );

        return deviceStatDTO;
    }

    @Override
    public List<DeviceStat> toEntity(List<DeviceStatDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DeviceStat> list = new ArrayList<DeviceStat>();
        for ( DeviceStatDTO deviceStatDTO : dtoList ) {
            list.add( toEntity( deviceStatDTO ) );
        }

        return list;
    }

    @Override
    public List<DeviceStatDTO> toDto(List<DeviceStat> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DeviceStatDTO> list = new ArrayList<DeviceStatDTO>();
        for ( DeviceStat deviceStat : entityList ) {
            list.add( toDto( deviceStat ) );
        }

        return list;
    }
}
