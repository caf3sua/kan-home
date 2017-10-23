package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.TopicService;
import com.opisvn.kanhome.domain.Topic;
import com.opisvn.kanhome.repository.TopicRepository;
import com.opisvn.kanhome.service.dto.TopicDTO;
import com.opisvn.kanhome.service.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing Topic.
 */
@Service
public class TopicServiceImpl implements TopicService{

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    /**
     *  Get all the topics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        return topicRepository.findAll(pageable)
            .map(topicMapper::toDto);
    }

    /**
     *  Get one topic by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public TopicDTO findOne(String id) {
        log.debug("Request to get Topic : {}", id);
        Topic topic = topicRepository.findOne(id);
        return topicMapper.toDto(topic);
    }

    /**
     *  Delete the  topic by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.delete(id);
    }
}
