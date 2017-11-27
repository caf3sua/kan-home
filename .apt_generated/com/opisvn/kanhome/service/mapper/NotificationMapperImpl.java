package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.Notification;
import com.opisvn.kanhome.service.dto.NotificationDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-11-27T23:13:06+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setId( dto.getId() );
        notification.setName( dto.getName() );
        notification.setTitle( dto.getTitle() );
        notification.setContent( dto.getContent() );
        notification.setEpoch( dto.getEpoch() );

        return notification;
    }

    @Override
    public NotificationDTO toDto(Notification entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId( entity.getId() );
        notificationDTO.setName( entity.getName() );
        notificationDTO.setTitle( entity.getTitle() );
        notificationDTO.setContent( entity.getContent() );
        notificationDTO.setEpoch( entity.getEpoch() );

        return notificationDTO;
    }

    @Override
    public List<Notification> toEntity(List<NotificationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Notification> list = new ArrayList<Notification>();
        for ( NotificationDTO notificationDTO : dtoList ) {
            list.add( toEntity( notificationDTO ) );
        }

        return list;
    }

    @Override
    public List<NotificationDTO> toDto(List<Notification> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NotificationDTO> list = new ArrayList<NotificationDTO>();
        for ( Notification notification : entityList ) {
            list.add( toDto( notification ) );
        }

        return list;
    }
}
