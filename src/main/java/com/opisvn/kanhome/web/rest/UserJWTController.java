package com.opisvn.kanhome.web.rest;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.security.UserNotActivatedException;
import com.opisvn.kanhome.security.jwt.JWTConfigurer;
import com.opisvn.kanhome.security.jwt.TokenProvider;
import com.opisvn.kanhome.service.UserService;
import com.opisvn.kanhome.web.rest.vm.LoginVM;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;
    
    private final UserService userService;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping({"/authenticate", "/v1/auth/login"})
    @Timed
    @ApiResponses( {
        @ApiResponse( code = 401, message = "BadCredentialsException - wrong username or password" )   
        , @ApiResponse( code = 404, message = "NoSuchElementException - User not found" )
        , @ApiResponse( code = 417, message = "UserNotActivatedException - User have not actived yet" )
    } )
    public ResponseEntity authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            // Get user
            Optional<User> user = userService.getUserWithAuthoritiesByLogin(loginVM.getUsername());
            return ResponseEntity.ok(new JWTToken(jwt, user.get()));
        } catch (AuthenticationException ae) {
            log.trace("Authentication exception trace: {}", ae);
            if (ae.getCause() instanceof BadCredentialsException) {
            	// 401 Unauthorized
            	return new ResponseEntity<>(Collections.singletonMap("BadCredentialsException",
                        ae.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        	} else if (ae.getCause() instanceof NoSuchElementException) {
        		// 404 Not found
        		return new ResponseEntity<>(Collections.singletonMap("NoSuchElementException",
                        ae.getLocalizedMessage()), HttpStatus.NOT_FOUND);
        	} else if (ae.getCause() instanceof UserNotActivatedException) {
        		// 417 Expectation Failed
        		return new ResponseEntity<>(Collections.singletonMap("UserNotActivatedException",
                        ae.getLocalizedMessage()), HttpStatus.EXPECTATION_FAILED);
        	}
            return new ResponseEntity<>(Collections.singletonMap("UnexpectedException",
                    ae.getLocalizedMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        
        private User user;

        JWTToken(String idToken, User user) {
            this.idToken = idToken;
            this.user = user;
        }

        @JsonProperty("user")
        User getUser() {
            return user;
        }

        void setUser(User user) {
            this.user = user;
        }
        
        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
