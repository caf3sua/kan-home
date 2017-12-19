package com.opisvn.kanhome.service.mapper;

import com.opisvn.kanhome.domain.Topic;
import com.opisvn.kanhome.service.dto.TopicDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-12-15T13:25:46+0700",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.12.3.v20170228-1205, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class TopicMapperImpl implements TopicMapper {

    @Override
    public Topic toEntity(TopicDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Topic topic = new Topic();

        topic.setId( dto.getId() );
        topic.setName( dto.getName() );
        topic.setTopics( dto.getTopics() );

        return topic;
    }

    @Override
    public TopicDTO toDto(Topic entity) {
        if ( entity == null ) {
            return null;
        }

        TopicDTO topicDTO = new TopicDTO();

        topicDTO.setId( entity.getId() );
        topicDTO.setName( entity.getName() );
        topicDTO.setTopics( entity.getTopics() );

        return topicDTO;
    }

    @Override
    public List<Topic> toEntity(List<TopicDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Topic> list = new ArrayList<Topic>();
        for ( TopicDTO topicDTO : dtoList ) {
            list.add( toEntity( topicDTO ) );
        }

        return list;
    }

    @Override
    public List<TopicDTO> toDto(List<Topic> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TopicDTO> list = new ArrayList<TopicDTO>();
        for ( Topic topic : entityList ) {
            list.add( toDto( topic ) );
        }

        return list;
    }
}
