package com.opisvn.kanhome.config.dbmigrations;

import com.opisvn.kanhome.domain.Authority;
import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.security.AuthoritiesConstants;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

/**
 * Creates the initial database setup
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

//    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
//        Authority adminAuthority = new Authority();
//        adminAuthority.setName(AuthoritiesConstants.ADMIN);
//        Authority userAuthority = new Authority();
//        userAuthority.setName(AuthoritiesConstants.USER);
//        Authority technicalAuthority = new Authority();
//        technicalAuthority.setName(AuthoritiesConstants.TECHNICAL);
//        Authority supportorAuthority = new Authority();
//        supportorAuthority.setName(AuthoritiesConstants.SUPPORTOR);

//        mongoTemplate.save(adminAuthority);
//        mongoTemplate.save(userAuthority);
//        mongoTemplate.save(technicalAuthority);
//        mongoTemplate.save(supportorAuthority);
    }

//    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
//    public void addUsers(MongoTemplate mongoTemplate) {
//        Authority adminAuthority = new Authority();
//        adminAuthority.setName(AuthoritiesConstants.ADMIN);
//        Authority userAuthority = new Authority();
//        userAuthority.setName(AuthoritiesConstants.USER);
//
//        User systemUser = new User();
//        systemUser.setId("user-0");
//        systemUser.setUsername("system");
//        systemUser.setPassword("MjVkNTVhZDI4M2FhNDAwYWY0NjRjNzZkNzEzYzA3YWQ=");
//        systemUser.setFirstName("");
//        systemUser.setLastName("System");
//        systemUser.setEmail("system@localhost");
//        systemUser.setActivated(true);
//        systemUser.setLangKey("en");
//        systemUser.setCreatedBy(systemUser.getUsername());
//        systemUser.setCreatedDate(Instant.now());
//        systemUser.getAuthorities().add(adminAuthority);
//        systemUser.getAuthorities().add(userAuthority);
//        mongoTemplate.save(systemUser);
//
//        User anonymousUser = new User();
//        anonymousUser.setId("user-1");
//        anonymousUser.setUsername("anonymoususer");
//        anonymousUser.setPassword("MjVkNTVhZDI4M2FhNDAwYWY0NjRjNzZkNzEzYzA3YWQ=");
//        anonymousUser.setFirstName("Anonymous");
//        anonymousUser.setLastName("User");
//        anonymousUser.setEmail("anonymous@localhost");
//        anonymousUser.setActivated(true);
//        anonymousUser.setLangKey("en");
//        anonymousUser.setCreatedBy(systemUser.getUsername());
//        anonymousUser.setCreatedDate(Instant.now());
//        mongoTemplate.save(anonymousUser);
//
//        User adminUser = new User();
//        adminUser.setId("user-2");
//        adminUser.setUsername("admin");
//        adminUser.setPassword("MjVkNTVhZDI4M2FhNDAwYWY0NjRjNzZkNzEzYzA3YWQ=");
//        adminUser.setFirstName("admin");
//        adminUser.setLastName("Administrator");
//        adminUser.setEmail("admin@localhost");
//        adminUser.setActivated(true);
//        adminUser.setLangKey("en");
//        adminUser.setCreatedBy(systemUser.getUsername());
//        adminUser.setCreatedDate(Instant.now());
//        adminUser.getAuthorities().add(adminAuthority);
//        adminUser.getAuthorities().add(userAuthority);
//        mongoTemplate.save(adminUser);
//
//        User userUser = new User();
//        userUser.setId("user-3");
//        userUser.setUsername("user");
//        userUser.setPassword("MjVkNTVhZDI4M2FhNDAwYWY0NjRjNzZkNzEzYzA3YWQ=");
//        userUser.setFirstName("");
//        userUser.setLastName("User");
//        userUser.setEmail("user@localhost");
//        userUser.setActivated(true);
//        userUser.setLangKey("en");
//        userUser.setCreatedBy(systemUser.getUsername());
//        userUser.setCreatedDate(Instant.now());
//        userUser.getAuthorities().add(userAuthority);
//        mongoTemplate.save(userUser);
//    }

}
