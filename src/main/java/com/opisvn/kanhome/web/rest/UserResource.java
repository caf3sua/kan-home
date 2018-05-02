package com.opisvn.kanhome.web.rest;

import com.opisvn.kanhome.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.domain.Authority;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.security.AuthoritiesConstants;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.DeviceService;
import com.opisvn.kanhome.service.MailService;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.service.dto.DeviceDTO;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.dto.UserDeviceDTO;
import com.opisvn.kanhome.service.mapper.UserMapper;
import com.opisvn.kanhome.service.util.KanhomeUtil;
import com.opisvn.kanhome.web.rest.vm.DeviceVM;
import com.opisvn.kanhome.web.rest.vm.ManagedUserVM;
import com.opisvn.kanhome.web.rest.vm.UpdatedUserVM;
import com.opisvn.kanhome.web.rest.vm.UserSearchVM;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "userManagement";

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserService userService;
    
    private final DeviceService deviceService;

    public UserResource(UserRepository userRepository, MailService mailService,
            UserService userService, DeviceService deviceService) {

        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.deviceService = deviceService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        // NamNH : 6/2/2018 - Set username = phonenumber
        if (org.apache.commons.lang3.StringUtils.isEmpty(managedUserVM.getUsername())) {
        	// Format VN phonenumber
        	String phonenumber = KanhomeUtil.formattVietnamPhoneNumber(managedUserVM.getPhonenumber());
        	managedUserVM.setUsername(phonenumber);
        }
        
        if (managedUserVM.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body(null);
        // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByUsername(managedUserVM.getUsername().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserVM);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getUsername()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     * 
     */
    @PutMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
        }
        existingUser = userRepository.findOneByUsername(managedUserVM.getUsername().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
        }
        Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", managedUserVM.getUsername()));
    }
    
    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     * @throws URISyntaxException 
     */
    @PostMapping("/v1/user/update")
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - User not found" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity<User> updateUserByUsername(@Valid @RequestBody UpdatedUserVM managedUserVM) throws URISyntaxException {
    	log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "user not found", "User not found")).body(null);
        }
        
        // Update properties
        User user = existingUser.get();
        
        // Set data
        if (StringUtils.isNotEmpty(managedUserVM.getFullname())) {
        	user.setFullname(managedUserVM.getFullname());
        }
        
        if (StringUtils.isNotEmpty(managedUserVM.getEmail())) {
        	user.setEmail(managedUserVM.getEmail());
        }
        
        if (StringUtils.isNotEmpty(managedUserVM.getPhonenumber())) {
        	user.setPhonenumber(managedUserVM.getPhonenumber());
        }
        
        if (StringUtils.isNotEmpty(managedUserVM.getAddress())) {
        	user.setAddress(managedUserVM.getAddress());
        }
        
        if (StringUtils.isNotEmpty(managedUserVM.getLangKey())) {
        	user.setLangKey(managedUserVM.getLangKey());
        }
        
        if (managedUserVM.getBirthday() != null) {
        	user.setBirthday(managedUserVM.getBirthday());
        }
        user.setGender(managedUserVM.getGender());
        User updateUser = userRepository.save(user); 

        return ResponseEntity.created(new URI("/api/users/" + updateUser.getUsername()))
                .headers(HeaderUtil.createAlert( "userManagement.created", updateUser.getUsername()))
                .body(updateUser);
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
    
    /**
     * POST /delete_users : delete the "login" Users.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/delete_users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUsers(@RequestBody String usernames) {
        log.debug("REST request to delete Users: {}", usernames);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(usernames)) {
        	String[] usernameArr = usernames.split(",");
        	for (String username : usernameArr) {
        		userService.deleteUser(username);
			}
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleteds", usernames)).build();
    }
    
    
    private boolean isExistDevice(String id) {
    	DeviceDTO device = deviceService.findOne(id);
    	if (device != null) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * POST  /users_update_devices  : Update device into user
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users_update_devices")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity updateUserDevice(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to updateUserDevice UserDTO : {}", userDTO);

        if (userDTO.getUsername() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body(null);
        }
        
        User user = userRepository.findOne(userDTO.getId());
        if (user == null) {
        	return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                    .body(null);
        }
        
        // Set name
        List<UserDeviceDTO> lstUserDevices = userDTO.getUserDevices();
        
        // Check ID of device is valid or invalid?
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO userDeviceDTO : user.getUserDevices()) {
    			// Check exist
        		boolean isExist = isExistDevice(userDeviceDTO.getId());
        		if (isExist == false) {
        			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
        					"idnotexists", "Device not found")).body(null);
        		}
    		}
    	}
        
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO userDeviceDTO : lstUserDevices) {
    			for (UserDeviceDTO udEntity : user.getUserDevices()) {
    				if (StringUtils.equals(userDeviceDTO.getId(), udEntity.getId())) {
    					userDeviceDTO.setName(udEntity.getName());
    				}
    			}
    		}
    	}
        
        // Update user device
        user.setUserDevices(lstUserDevices);
        userRepository.save(user);
        
        return ResponseEntity.created(new URI("/api/users/" + user.getUsername()))
            .headers(HeaderUtil.createAlert( "userManagement.updated", user.getUsername()))
            .body(user);
    }
    
    @PostMapping({"/users_change_device_name", "/v1/user/changeCustomeDeviceName"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - User not found" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity changeUserDeviceName(@Valid @RequestBody UserDeviceDTO userDeviceDTO) throws URISyntaxException {
        log.debug("REST request to changeUserDeviceName UserDeviceDTO : {}", userDeviceDTO);

        if (userDeviceDTO.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "User device can not empty"))
                .body(null);
        }

        String username = SecurityUtils.getCurrentUserLogin();
		
		// User
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User not found")).body(null);
		}
		
		User user = userOpt.get();
        
        // Find exits
        List<UserDeviceDTO> lstUserDevices = user.getUserDevices();
        boolean isCanChange = false;
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO obj : lstUserDevices) {
				if (StringUtils.equals(obj.getId(), userDeviceDTO.getId())) {
					obj.setName(userDeviceDTO.getName());;
					isCanChange = true;
					break;
				}
    		}
    	}
        
        if (isCanChange == false) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User device not found")).body(null);
        }
        
        // Update
        user.setUserDevices(lstUserDevices);
        userRepository.save(user);
        
        return ResponseEntity.created(new URI("/api/users/" + user.getUsername()))
            .headers(HeaderUtil.createAlert( "userManagement.updated", user.getUsername()))
            .body(user);
    }
    
    @PostMapping({"/users_add_device", "/v1/user/addDevice"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - User not found" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity addUserDevice(@Valid @RequestBody UserDeviceDTO userDeviceDTO) throws URISyntaxException {
        log.debug("REST request to addUserDevice, username: {}, UserDeviceDTO : {}", SecurityUtils.getCurrentUserLogin(), userDeviceDTO);

        if (userDeviceDTO.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "User device can not empty"))
                .body(null);
        }

        String username = SecurityUtils.getCurrentUserLogin();
		
		// User
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User not found")).body(null);
		}
		
        User user = userOpt.get();
        
        // Check ID of device is valid or invalid?
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO item : user.getUserDevices()) {
    			// Check exist
        		boolean isExist = isExistDevice(item.getId());
        		if (isExist == false) {
        			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
        					"idnotexists", "Device not found")).body(null);
        		}
    		}
    	}
        
        // Find duplicate
        List<UserDeviceDTO> lstUserDevices = user.getUserDevices();
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO obj : lstUserDevices) {
				if (StringUtils.equals(obj.getId(), userDeviceDTO.getId())) {
					return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
							"idexists", "Userdevice is exists")).body(null);
				}
    		}
    	}
        
        // Update user device
        if (lstUserDevices == null) {
        	lstUserDevices = new ArrayList<>();
        }
        lstUserDevices.add(userDeviceDTO);
        
        // Update
        user.setUserDevices(lstUserDevices);
        userRepository.save(user);
        
        return ResponseEntity.created(new URI("/api/users/" + user.getUsername()))
            .headers(HeaderUtil.createAlert( "userManagement.updated", user.getUsername()))
            .body(user);
    }
    
    @PostMapping({"/users_remove_device", "/v1/user/removeDevice"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - User not found" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity removeUserDevice(@Valid @RequestBody UserDeviceDTO userDeviceDTO) throws URISyntaxException {
        log.debug("REST request to removeUserDevice UserDeviceDTO : {}", userDeviceDTO);

        if (userDeviceDTO.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "User device can not empty"))
                .body(null);
        }

        String username = SecurityUtils.getCurrentUserLogin();
		
		// User
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User not found")).body(null);
		}
		
        User user = userOpt.get();
        
        // Find exits
        List<UserDeviceDTO> lstUserDevices = user.getUserDevices();
        boolean isCanRemove = false;
        UserDeviceDTO removeObj = null;
        if (user.getUserDevices() != null && user.getUserDevices().size() > 0) {
        	for (UserDeviceDTO obj : lstUserDevices) {
				if (StringUtils.equals(obj.getId(), userDeviceDTO.getId())) {
					removeObj = obj;
					isCanRemove = true;
					break;
				}
    		}
    	}
        
        if (isCanRemove == false) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, 
					"idexists", "User device not found")).body(null);
        }
        
        // Update user device
        lstUserDevices.remove(removeObj);
        
        // Update
        user.setUserDevices(lstUserDevices);
        userRepository.save(user);
        
        return ResponseEntity.created(new URI("/api/users/" + user.getUsername()))
            .headers(HeaderUtil.createAlert( "userManagement.updated", user.getUsername()))
            .body(user);
    }
    
    /**
     * POST  /users-search : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @PostMapping("/users-search")
    @Timed
    public ResponseEntity<List<UserDTO>> searchAllUsers(@RequestBody UserSearchVM searchVM) {
    	log.debug("REST request to searchAllUsers");
    	String[] sorts = searchVM.getSort().get(0).split(",");
    	Sort sort = new Sort(Direction.fromString(sorts[1]), sorts[0]);
    	Pageable pageable = new PageRequest(searchVM.getPage(), searchVM.getSize(), sort);
    	
        Page<UserDTO> page = userService.search(pageable, searchVM.getSearch());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users-search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    /**
     * POST  /reset_pass  : Reset password
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reset_pass")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity resetPassword(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to resetPassword UserDTO : {}", userDTO);

        if (userDTO.getUsername() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body(null);
        }
        
        User user = userRepository.findOne(userDTO.getId());
        if (user == null) {
        	return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                    .body(null);
        }
        
        // Update user device
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        
        return ResponseEntity.created(new URI("/api/users/" + user.getUsername()))
            .headers(HeaderUtil.createAlert( "userManagement.updated", user.getUsername()))
            .body(user);
    }
}
