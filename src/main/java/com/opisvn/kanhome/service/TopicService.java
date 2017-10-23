package com.opisvn.kanhome.service;

import com.opisvn.kanhome.service.dto.TopicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Topic.
 */
public interface TopicService {

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     *  Get all the topics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TopicDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" topic.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TopicDTO findOne(String id);

    /**
     *  Delete the "id" topic.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
