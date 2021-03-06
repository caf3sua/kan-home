
package com.opisvn.kanhome.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opisvn.kanhome.domain.ActiveCodes;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.repository.ActiveCodesRepository;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.MailService;
import com.opisvn.kanhome.service.SmsService;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.service.dto.ActivedUserDTO;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.util.KanhomeUtil;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.vm.KeyAndPasswordVM;
import com.opisvn.kanhome.web.rest.vm.ManagedUserVM;
import com.opisvn.kanhome.web.rest.vm.PasswordVM;
import com.opisvn.kanhome.web.rest.vm.RegisterUserVM;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;
    
    private final SmsService smsService;
    
    private final AuditEventRepository auditEventRepository;
    
    private final ActiveCodesRepository activeCodesRepository;
    
    private final Environment env;

    public AccountResource(UserRepository userRepository, UserService userService,
            MailService mailService, SmsService smsService, AuditEventRepository auditEventRepository, ActiveCodesRepository activeCodesRepository
            , Environment env) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.smsService = smsService;
        this.auditEventRepository = auditEventRepository;
        this.activeCodesRepository = activeCodesRepository;
        this.env = env;
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping(path = {"/register", "/v1/user/create"},
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 409, message = "phonenumber/email already in use" )
    } )
    public ResponseEntity registerAccount(@Valid @RequestBody RegisterUserVM managedUserVM) {
    		log.debug("REST request to registerAccount : {}", managedUserVM);
    	
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        
        // NamNH : 6/2/2018 - Set username = phonenumber
        if (org.apache.commons.lang3.StringUtils.isEmpty(managedUserVM.getUsername())) {
        	// Format VN phonenumber
        	String phonenumber = KanhomeUtil.formattVietnamPhoneNumber(managedUserVM.getPhonenumber());
        	managedUserVM.setUsername(phonenumber);
        }

        return userRepository.findOneByUsername(StringUtils.lowerCase(managedUserVM.getUsername()))
            .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.CONFLICT))
            .orElseGet(() -> userRepository.findOneByEmail(managedUserVM.getEmail())
                .map(user -> new ResponseEntity<>("email address already in use", textPlainHeaders, HttpStatus.CONFLICT))
                .orElseGet(() -> {
                    User user = userService
                        .createUser(StringUtils.lowerCase(managedUserVM.getUsername()), managedUserVM.getPassword(),
                            managedUserVM.getPhonenumber(), managedUserVM.getEmail(),
                            managedUserVM.getLangKey());

                    // Send SMS exclude Myanmar
    				if (KanhomeUtil.isMyanmarPhoneNumber(user.getPhonenumber())) {
    					log.debug("MYANMAR phone number {}, will not send SMS", user.getPhonenumber());
    				} else {
    					boolean result = smsService.sendSMS(user.getPhonenumber(), user.getActivationCode());
        				log.debug("Result send SMS: {}", result);
        				
        				//  and Send mail
    					// Create sms code and send mail
    					mailService.sendActivationEmail(user);
    				}
					
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
        );
    }
    
//    private void validateCreateUser(UserDTO dto) throws Exception {
//		// validate phonenumber (1 number allow register 5 acc)
//		long countPhonenumber = userService.countByPhonenumber(dto.getPhonenumber());
//		
//		if (countPhonenumber >= 5) {
//			throw new Exception("Reach limit phonenumber");
//		}
//	}

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
    	log.debug("REST request to get current Account");
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @PostMapping("/account")
    @Timed
    public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
    		log.debug("REST request to saveAccount, {}", userDTO);
        final String userLogin = StringUtils.lowerCase(SecurityUtils.getCurrentUserLogin());
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getUsername().equalsIgnoreCase(userLogin))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
            .findOneByUsername(userLogin)
            .map(u -> {
                userService.updateUser(userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhonenumber(),
                    userDTO.getAddress(), userDTO.getGender(), userDTO.getBirthday(), userDTO.getLangKey());
                return new ResponseEntity(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = {"/account/change_password", "/v1/user/changePwd"},
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 403, message = "Forbidden - Old password wrong" )
        , @ApiResponse( code = 400, message = "Bad request - Password is invalid" )
        , @ApiResponse( code = 200, message = "Change password success" )
    } )
    public ResponseEntity changePassword(@RequestBody PasswordVM password) {
	    	log.debug("REST request to changePassword, {}", password);
	    	// Check old pass
	    	Optional<User> optUser = userService.getUserWithAuthoritiesByLogin(StringUtils.lowerCase(SecurityUtils.getCurrentUserLogin()));
	    	User user = optUser.get();
	    	
	    	if (!StringUtils.equals(password.getOldPassword(), user.getPassword())) {
	    		return new ResponseEntity<>("Old password wrong", HttpStatus.FORBIDDEN);
	    	}
	    	
        if (!checkPasswordLength(password.getPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @return the ResponseEntity with status 200 (OK) if the email was sent, or status 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = {"/account/reset_password/init", "/v1/user/forgetPwd"},
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - email address not registered or account not yet actived" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity requestPasswordReset(@RequestBody String mail) {
    		log.debug("REST request to requestPasswordReset, email: {}", mail);
        return userService.requestPasswordReset(mail)
            .map(user -> {
                mailService.sendPasswordResetMail(user);
                return new ResponseEntity<>("email was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("email address not registered or account not yet actived", HttpStatus.BAD_REQUEST));
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = {"/account/reset_password/finish", "/v1/user/recoveryPwd"},
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 400, message = "Bad request - password is invalid" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
    		log.debug("REST request to finishPasswordReset, {}", keyAndPassword);
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
              .map(user -> new ResponseEntity<String>(HttpStatus.OK))
              .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
    
    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
    		log.debug("REST request to activateAccount, {}", key);
        return userService.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
    
    /**
     * GET  /account/activate?sms=:sms&username=:username : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     * @throws URISyntaxException 
     */
    @GetMapping({"/account/activate", "/v1/account/activate"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 404, message = "User not found" )
        , @ApiResponse( code = 400, message = "Bad request - User activated" )
        , @ApiResponse( code = 406, message = "Not Acceptable - Sms code is not found, activation fail" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity<User> activateAccountBySmsCode(@RequestParam(value = "username", required=true) String username
    		, @RequestParam(value = "sms", required=true) final String sms) throws URISyntaxException {
    		log.debug("REST request to activateAccountBySmsCode, username : {}, sms : {}", username, sms);
    	
		// NamNH : 6/2/2018 - format username/phonenumber
        if (KanhomeUtil.isVietnamPhoneNumber(username)) {
        	// Format VN phonenumber
        	username = KanhomeUtil.formattVietnamPhoneNumber(username);
        }
            
    	// Check user exist
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(StringUtils.lowerCase(username));
		if (!userOpt.isPresent() || userOpt.get() == null) {
			return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert("User", 
					"usernotfound", "User " + username + " not found")).build();
		}
		
		User user = userOpt.get();
		// Check activated?
		if (user.getActivated()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"activated", "User " + username + " activated")).body(null);
		}
		
		
		// Compare sms code if not Myanmar
		if (KanhomeUtil.isMyanmarPhoneNumber(username)) {
			// Validate Sms Code in collection activeCodes
			boolean checkActiveCode = checkSmsInActiveCode(sms, username);
			if (checkActiveCode == false) {
				// Audit and log
				String errorMsg = "message=sms code: " + sms + "not found in ActiveCodes collection";
				AuditEvent event = new AuditEvent(username, "ACTIVATED_FAILED", errorMsg); 
				auditEventRepository.add(event);
				log.debug("Active account FAILED, username: {}, sms code: {} not found", username, sms);
//				return ResponseEntity. .badRequest().headers(HeaderUtil.createFailureAlert("User", 
//						"smscodeinvalid", "Sms code is not found")).body(null);
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
						.headers(HeaderUtil.createFailureAlert("User", "smscodeinvalid", "Sms code is not found")).body(null);
			}
		} else {
			// Check activation code
			if (!StringUtils.equalsIgnoreCase(sms, user.getActivationCode())) {
				// If false, check in activeCode
				// Validate Sms Code in collection activeCodes
				boolean checkActiveCode = checkSmsInActiveCode(sms, username);
				
				if (checkActiveCode == false) {
					// Audit and log
					String errorMsg = "message=sms code: " + sms +  " but expect: " + user.getActivationCode() + "and not found or remain = 0 in ActiveCodes collection";
					AuditEvent event = new AuditEvent(username, "ACTIVATED_FAILED", errorMsg); 
					auditEventRepository.add(event);
					log.debug("Active account FAILED, username: {}, sms code: {} not found", username, sms);
//					return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
//							"smscodeinvalid", "Sms code is not found")).body(null);
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
							.headers(HeaderUtil.createFailureAlert("User", "smscodeinvalid", "Sms code is not found")).body(null);
				}
			}
		}
		
		// Update status and remove activation code
        user.setActivated(true);
        user.setActivationCode(null);
        userRepository.save(user);
        log.debug("Activated user: {}", user);
		
        return ResponseEntity.created(new URI("/api/account/activate" + user.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("User", user.getId().toString()))
                .body(user);
    }
    
    
    private boolean checkSmsInActiveCode(String sms, String username) {
    	ActiveCodes activeCode = activeCodesRepository.findOneByActiveCode(sms);
		if (activeCode == null || activeCode.getRemain() == 0) {
			return false;
		} else {
			// Update information remain and user
			int remain = activeCode.getRemain() - 1;
			activeCode.setRemain(remain);
			// Insert user active
			ActivedUserDTO activeUser = new ActivedUserDTO();
			activeUser.setUser(username);
			activeUser.setCreated_date(new Date());
			activeCode.getActivedUsers().add(activeUser);
			activeCodesRepository.save(activeCode);
		}
		
		return true;
    }
    
    @GetMapping({"/account/resend_sms", "/v1/account/resend-sms"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 404, message = "User not found" )
        , @ApiResponse( code = 400, message = "Bad request - belong to Myanmar country" )
        , @ApiResponse( code = 200, message = "Success" )
    } )
    public ResponseEntity<User> resendSmsCode(@RequestParam(value = "username", required=true) final String username) throws URISyntaxException {
    	
    	log.debug("Start method resendSmsCode, username: {}", StringUtils.lowerCase(username));
    	
    	// NamNH: 6/2/2018 - No send if Myanmar
        if (KanhomeUtil.isMyanmarPhoneNumber(username)) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"resend", "Username: " + username + " belong to Myanmar country")).body(null);
        }
    	
		// Update user status = 0
    		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(StringUtils.lowerCase(username));
		if (!userOpt.isPresent() || userOpt.get() == null) {
			return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert("User", 
					"usernotfound", "User " + username + " not found")).build();
		}
		
		User user = userOpt.get();
		// Check activated?
		if (user.getActivated()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"activated", "User " + username + " activated")).body(null);
		}
		
		// Validate number of resend
	       
		int iResendSms = Integer.valueOf(env.getProperty("spring.sms.number-resend"));
		if (user.getNumberSendSms() != null && user.getNumberSendSms() >= iResendSms) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"max-resend-sms", "User " + username + " has reached max resend sms code")).body(null);
		}
		
		// Generate new SMS code
		// String smsCode = KanhomeUtil.generateSmsCode();

		// Send SMS
		boolean result = smsService.sendSMS(user.getPhonenumber(), user.getActivationCode());
		log.debug("Result re-send SMS: {} of user :{}, phonenumber: {}", result, username, user.getPhonenumber());

		// Update status and add activation code
		// user.setActivationCode(smsCode);
		if (user.getNumberSendSms() == null) {
			user.setNumberSendSms(1);
		} else {
			user.setNumberSendSms(user.getNumberSendSms() + 1);
		}
		
		
		//  and Send mail
		if (result) {
			mailService.sendSmsMail(user);
		}
		
		// Update
		User resultUser = userRepository.save(user);
		
		return ResponseEntity.created(new URI("/api/v1/account/resend-sms" + user.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("User", user.getId().toString()))
                .body(resultUser);
    }
}
