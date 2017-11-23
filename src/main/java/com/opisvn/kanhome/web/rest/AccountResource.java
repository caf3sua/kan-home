
package com.opisvn.kanhome.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.MailService;
import com.opisvn.kanhome.service.SmsService;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.util.KanhomeUtil;
import com.opisvn.kanhome.web.rest.util.HeaderUtil;
import com.opisvn.kanhome.web.rest.vm.KeyAndPasswordVM;
import com.opisvn.kanhome.web.rest.vm.ManagedUserVM;
import com.opisvn.kanhome.web.rest.vm.PasswordVM;

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

    public AccountResource(UserRepository userRepository, UserService userService,
            MailService mailService, SmsService smsService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.smsService = smsService;
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
    public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
    		log.debug("REST request to registerAccount : {}", managedUserVM);
    	
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

        return userRepository.findOneByUsername(managedUserVM.getUsername().toLowerCase())
            .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(managedUserVM.getEmail())
                .map(user -> new ResponseEntity<>("email address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    User user = userService
                        .createUser(managedUserVM.getUsername(), managedUserVM.getPassword(),
                            managedUserVM.getPhonenumber(), managedUserVM.getEmail(),
                            managedUserVM.getLangKey());

                    // Send SMS
    				boolean result = smsService.sendSMS(user.getPhonenumber(), user.getActivationCode());
    				log.debug("Result send SMS: {}", result);
    				
    				//  and Send mail
					// Create sms code and send mail
					mailService.sendActivationEmail(user);
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
        final String userLogin = SecurityUtils.getCurrentUserLogin();
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
    public ResponseEntity changePassword(@RequestBody PasswordVM password) {
	    	log.debug("REST request to changePassword, {}", password);
	    	// Check old pass
	    	Optional<User> optUser = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin());
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
    public ResponseEntity requestPasswordReset(@RequestBody String mail) {
    		log.debug("REST request to requestPasswordReset, {}", mail);
        return userService.requestPasswordReset(mail)
            .map(user -> {
                mailService.sendPasswordResetMail(user);
                return new ResponseEntity<>("email was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST));
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
    public ResponseEntity<User> activateAccountBySmsCode(@RequestParam(value = "username", required=true) final String username
    		, @RequestParam(value = "sms", required=true) final String sms) throws URISyntaxException {
    		log.debug("REST request to activateAccountBySmsCode, username : {}, sms : {}", username, sms);
    	
		Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"idexists", "User not found")).body(null);
		}
		
		User user = userOpt.get();
		// Compare sms code
		if (!StringUtils.equalsIgnoreCase(sms, user.getActivationCode())) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"idexists", "Sms code is invalid")).body(null);
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
    
    @GetMapping({"/account/resend_sms", "/v1/account/resend-sms"})
    @Timed
    public ResponseEntity<User> resendSmsCode(@RequestParam(value = "username", required=true) final String username) throws URISyntaxException {
    	
    	log.debug("Start method resendSmsCode, username: {}", username);
		// Update user status = 0
    	Optional<User> userOpt  = userService.getUserWithAuthoritiesByLogin(username);
		if (userOpt.get() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", 
					"idexists", "User not found")).body(null);
		}
		
		User user = userOpt.get();
		
		// Send SMS
		String smsCode = KanhomeUtil.generateSmsCode();

		// Send SMS
		boolean result = smsService.sendSMS(user.getPhonenumber(), smsCode);
		log.debug("Result send SMS: {}", result);

		// Update status and add activation code
		user.setActivationCode(smsCode);
		
		//  and Send mail
		if (result) {
			mailService.sendSmsMail(user);
		}
		
		// Update
		User resultUser = userRepository.save(user);
		
		return ResponseEntity.created(new URI("/api/account/activate" + user.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("User", user.getId().toString()))
                .body(resultUser);
    }
}
