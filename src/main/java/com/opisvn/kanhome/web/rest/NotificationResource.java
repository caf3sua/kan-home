package com.opisvn.kanhome.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.NotificationService;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.util.PaginationUtil;
import com.opisvn.kanhome.service.dto.NotificationDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    private final NotificationService notificationService;
    
    private MqttAsyncClient mqttAsyncClient;
    
    private final UserService userService;

    public NotificationResource(NotificationService notificationService, MqttAsyncClient mqttAsyncClient, UserService userService) {
        this.notificationService = notificationService;
        this.mqttAsyncClient = mqttAsyncClient;
        this.userService = userService;
    }

    /**
     * POST  /notifications : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationDTO, or with status 400 (Bad Request) if the notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws MqttException 
     * @throws MqttPersistenceException 
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new notification cannot already have an ID")).body(null);
        }
        // Get max resource
        Long maxId = notificationService.getMaxId() + 1;
        notificationDTO.setId(maxId);
        
        NotificationDTO result = notificationService.save(notificationDTO);
        
        // MQTT notification
        try {
	        MqttMessage message = new MqttMessage();
	        message.setPayload("NEW_NOTIFCATION".getBytes());
	        if (mqttAsyncClient.isConnected()) {
	        	mqttAsyncClient.publish("notification", message);
	        }
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
        
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationDTO,
     * or with status 400 (Bad Request) if the notificationDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws MqttException 
     * @throws MqttPersistenceException 
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO) throws URISyntaxException, MqttPersistenceException, MqttException {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            return createNotification(notificationDTO);
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/notifications")
    @Timed
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Notifications");
        Page<NotificationDTO> page = notificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifications/:id : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        NotificationDTO notificationDTO = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificationDTO));
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notification.
     *
     * @param id the id of the notificationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
    
    @GetMapping({"/notifications_latest", "/v1/notification/latest"})
    @Timed
	public ResponseEntity<List<NotificationDTO>> getLatestNotification(@RequestParam(value = "fromId", required=false) final String fromId) {
		log.debug("REST request to getLatestNotification : {}", fromId);
		
		String username = SecurityUtils.getCurrentUserLogin();
		
		// User
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User not found")).body(null);
		}
		
		List<NotificationDTO> lstNotification = null;
		Long latestNotificationId = userOpt.get().getLatestNotificationId();
		if (null == latestNotificationId || latestNotificationId == 0) {
			// Get all
			lstNotification = notificationService.findAll();
		} else {
			// Get all
			lstNotification = notificationService.findFromId(String.valueOf(latestNotificationId));
		}
		
		// Update user
		updateNotificationId(userOpt.get(), lstNotification);
		
		return new ResponseEntity<>(lstNotification, null, HttpStatus.OK);
	}
    
    private void updateNotificationId(User user, List<NotificationDTO> lstNotification) {
		if (null == lstNotification || lstNotification.size() == 0) {
			return;
		}
		
		Long result = 0l;
		for (NotificationDTO notification : lstNotification) {
			if (notification.getId() > result) {
				result = notification.getId();
			}
		}
		
		user.setLatestNotificationId(result);
		userService.updateUser(result);
	}
}
