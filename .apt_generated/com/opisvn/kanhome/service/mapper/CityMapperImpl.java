package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.City;
import com.opisvn.kanhome.service.dto.CityDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-19T22:30:23+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class CityMapperImpl implements CityMapper {

    @Override
    public City toEntity(CityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        City city = new City();

        city.setId( dto.getId() );
        city.setName( dto.getName() );
        city.setCountryCode( dto.getCountryCode() );
        city.setDescription( dto.getDescription() );

        return city;
    }

    @Override
    public CityDTO toDto(City entity) {
        if ( entity == null ) {
            return null;
        }

        CityDTO cityDTO = new CityDTO();

        cityDTO.setId( entity.getId() );
        cityDTO.setName( entity.getName() );
        cityDTO.setCountryCode( entity.getCountryCode() );
        cityDTO.setDescription( entity.getDescription() );

        return cityDTO;
    }

    @Override
    public List<City> toEntity(List<CityDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<City> list = new ArrayList<City>();
        for ( CityDTO cityDTO : dtoList ) {
            list.add( toEntity( cityDTO ) );
        }

        return list;
    }

    @Override
    public List<CityDTO> toDto(List<City> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CityDTO> list = new ArrayList<CityDTO>();
        for ( City city : entityList ) {
            list.add( toDto( city ) );
        }

        return list;
    }
}
