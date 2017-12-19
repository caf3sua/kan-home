package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.Country;
import com.opisvn.kanhome.service.dto.CountryDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-12-15T13:25:47+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class CountryMapperImpl implements CountryMapper {

    @Override
    public Country toEntity(CountryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Country country = new Country();

        country.setId( dto.getId() );
        country.setName( dto.getName() );
        country.setCountryCode( dto.getCountryCode() );
        country.setLanguageCode( dto.getLanguageCode() );
        country.setDescription( dto.getDescription() );
        country.setNote( dto.getNote() );

        return country;
    }

    @Override
    public CountryDTO toDto(Country entity) {
        if ( entity == null ) {
            return null;
        }

        CountryDTO countryDTO = new CountryDTO();

        countryDTO.setId( entity.getId() );
        countryDTO.setName( entity.getName() );
        countryDTO.setCountryCode( entity.getCountryCode() );
        countryDTO.setLanguageCode( entity.getLanguageCode() );
        countryDTO.setDescription( entity.getDescription() );
        countryDTO.setNote( entity.getNote() );

        return countryDTO;
    }

    @Override
    public List<Country> toEntity(List<CountryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Country> list = new ArrayList<Country>();
        for ( CountryDTO countryDTO : dtoList ) {
            list.add( toEntity( countryDTO ) );
        }

        return list;
    }

    @Override
    public List<CountryDTO> toDto(List<Country> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CountryDTO> list = new ArrayList<CountryDTO>();
        for ( Country country : entityList ) {
            list.add( toDto( country ) );
        }

        return list;
    }
}
