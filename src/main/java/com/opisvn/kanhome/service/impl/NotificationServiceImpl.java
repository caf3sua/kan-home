package com.opisvn.kanhome.service.impl;

import com.opisvn.kanhome.service.NotificationService;
import com.opisvn.kanhome.domain.Notification;
import com.opisvn.kanhome.repository.NotificationRepository;
import com.opisvn.kanhome.service.dto.NotificationDTO;
import com.opisvn.kanhome.service.mapper.NotificationMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing Notification.
 */
@Service
public class NotificationServiceImpl implements NotificationService{

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     *  Get all the notifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable)
            .map(notificationMapper::toDto);
    }

    /**
     *  Get one notification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public NotificationDTO findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return notificationMapper.toDto(notification);
    }

    /**
     *  Delete the  notification by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        Notification notification = notificationRepository.findOne(Long.valueOf(id));
        if (notification != null) {
        	notificationRepository.delete(notification);
        }
    }

	@Override
	public List<NotificationDTO> findAll() {
		log.debug("Request to get Notification findAll");
        List<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toDto(notifications);
	}

	@Override
	public List<NotificationDTO> findFromId(String fromId) {
		log.debug("Request to get Notification findFromId : {}", fromId);
        List<Notification> notifications = notificationRepository.findFromId(fromId);
        return notificationMapper.toDto(notifications);
	}

	@Override
	public Long getMaxId() {
		log.debug("Request to get Notification getMaxId");
        return notificationRepository.getMaxId();
	}
}
