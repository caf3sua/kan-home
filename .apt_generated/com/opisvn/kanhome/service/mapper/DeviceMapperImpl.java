package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.ParamDTO;
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
public class DeviceMapperImpl implements DeviceMapper {

    @Override
    public Device toEntity(DeviceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Device device = new Device();

        device.setId( dto.getId() );
        device.setName( dto.getName() );
        device.setAbbrName( dto.getAbbrName() );
        device.setModel( dto.getModel() );
        device.setFirmware( dto.getFirmware() );
        device.setPasscode( dto.getPasscode() );
        device.setCountryCode( dto.getCountryCode() );
        device.setCityCode( dto.getCityCode() );
        device.setWardCode( dto.getWardCode() );
        device.setZipCode( dto.getZipCode() );
        device.setGpsX( dto.getGpsX() );
        device.setGpsY( dto.getGpsY() );
        device.setSrvName( dto.getSrvName() );
        device.setSrvPort( dto.getSrvPort() );
        device.setRepotIntv( dto.getRepotIntv() );
        device.setSaveIntv( dto.getSaveIntv() );
        device.setKeepalive( dto.getKeepalive() );
        device.setDistCode( dto.getDistCode() );
        device.setCfgNum( dto.getCfgNum() );
        Map<Integer, ParamDTO> map = dto.getPara();
        if ( map != null ) {
            device.setPara(       new HashMap<Integer, ParamDTO>( map )
            );
        }

        return device;
    }

    @Override
    public DeviceDTO toDto(Device entity) {
        if ( entity == null ) {
            return null;
        }

        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId( entity.getId() );
        deviceDTO.setName( entity.getName() );
        deviceDTO.setAbbrName( entity.getAbbrName() );
        deviceDTO.setModel( entity.getModel() );
        deviceDTO.setFirmware( entity.getFirmware() );
        deviceDTO.setPasscode( entity.getPasscode() );
        deviceDTO.setCountryCode( entity.getCountryCode() );
        deviceDTO.setCityCode( entity.getCityCode() );
        deviceDTO.setWardCode( entity.getWardCode() );
        deviceDTO.setZipCode( entity.getZipCode() );
        deviceDTO.setGpsX( entity.getGpsX() );
        deviceDTO.setGpsY( entity.getGpsY() );
        deviceDTO.setSrvName( entity.getSrvName() );
        deviceDTO.setSrvPort( entity.getSrvPort() );
        deviceDTO.setRepotIntv( entity.getRepotIntv() );
        deviceDTO.setSaveIntv( entity.getSaveIntv() );
        deviceDTO.setKeepalive( entity.getKeepalive() );
        deviceDTO.setDistCode( entity.getDistCode() );
        deviceDTO.setCfgNum( entity.getCfgNum() );
        Map<Integer, ParamDTO> map = entity.getPara();
        if ( map != null ) {
            deviceDTO.setPara(       new HashMap<Integer, ParamDTO>( map )
            );
        }

        return deviceDTO;
    }

    @Override
    public List<Device> toEntity(List<DeviceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Device> list = new ArrayList<Device>();
        for ( DeviceDTO deviceDTO : dtoList ) {
            list.add( toEntity( deviceDTO ) );
        }

        return list;
    }

    @Override
    public List<DeviceDTO> toDto(List<Device> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DeviceDTO> list = new ArrayList<DeviceDTO>();
        for ( Device device : entityList ) {
            list.add( toDto( device ) );
        }

        return list;
    }
}
