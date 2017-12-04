package com.opisvn.kanhome.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.opisvn.kanhome.config.Constants;
import com.opisvn.kanhome.domain.Authority;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.repository.AuthorityRepository;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.security.AuthoritiesConstants;
import com.opisvn.kanhome.security.SecurityUtils;
import com.opisvn.kanhome.service.dto.UserDTO;
import com.opisvn.kanhome.service.util.KanhomeUtil;
import com.opisvn.kanhome.service.util.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SocialService socialService;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialService socialService, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.socialService = socialService;
        this.authorityRepository = authorityRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                //user.setPassword(passwordEncoder.encode(newPassword));
        	   user.setPassword(newPassword);
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                userRepository.save(user);
                return user;
            });
    }
    
    public User createUser(String username, String password, String phonenumber, String email,
            String langKey) {

            User newUser = new User();
            Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
            Set<Authority> authorities = new HashSet<>();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            if (StringUtils.isEmpty(langKey)) {
            	langKey = "vi";
            }
            newUser.setLangKey(langKey);
            newUser.setPhonenumber(phonenumber);
            // new user is not active
            newUser.setActivated(false);
            // new user gets registration key
            newUser.setActivationKey(RandomUtil.generateActivationKey());
            authorities.add(authority);
            newUser.setAuthorities(authorities);
            // SMS code
			String smsCode = KanhomeUtil.generateSmsCode();
			newUser.setActivationCode(smsCode);
			
            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
            return newUser;
    }

    public User createUser(String username, String password, String firstName, String lastName, String email,
        String imageUrl, String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUsername(username);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setImageUrl(imageUrl);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey("vi"); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            userRepository.save(user);
            log.debug("Changed Information for User: {}", user);
        });
    }
    
    public void updateUser(String fullname, String email, String phonenumber, String address, Integer gender, Date birthday, String langKey) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhonenumber(phonenumber);
            user.setAddress(address);
            user.setGender(gender);
            user.setLangKey(langKey);
            if (birthday != null) {
            	user.setBirthday(birthday);
            }
            userRepository.save(user);
            log.debug("Changed Information for User: {}", user);
        });
    }
    
    public void updateUser(Long latestNotificationId) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setLatestNotificationId(latestNotificationId);
            userRepository.save(user);
            log.debug("Changed Information for User: {}", user);
        });
    }
    
    public void updateActivationCode(String smsCode) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setActivationCode(smsCode);
            userRepository.save(user);
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
//                user.setUsername(userDTO.getUsername());
            	user.setFullname(userDTO.getFullname());
//                user.setFirstName(userDTO.getFirstName());
//                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setPhonenumber(userDTO.getPhonenumber());
                user.setGender(userDTO.getGender());
                user.setAddress(userDTO.getAddress());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                userRepository.save(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByUsername(login).ifPresent(user -> {
            socialService.deleteUserSocialConnection(user.getUsername());
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            //String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(password);
            userRepository.save(user);
            log.debug("Changed password for User: {}", user);
        });
    }

    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByUsernameNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByUsername(login);
    }

    public User getUserWithAuthorities(String id) {
        return userRepository.findOne(id);
    }

    public User getUserWithAuthorities() {
        return userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void removeNotActivatedUsers() {
//        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
//        for (User user : users) {
//            log.debug("Deleting not activated user {}", user.getUsername());
//            userRepository.delete(user);
//        }
//    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }
    
    public Page<UserDTO> search(Pageable pageable, UserDTO dto) {
        return userRepository.search(pageable, dto).map(UserDTO::new);
    }
}
