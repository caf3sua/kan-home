package com.opisvn.kanhome.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opisvn.kanhome.service.dto.ActivedUserDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * A Topic.
 */

@Document(collection = "activeCodes")
@Getter
@Setter
public class ActiveCodes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("activeCode")
    private String activeCode;

    @Field("remain")
    private Integer remain;
    
    @Field("activedUsers")
    private List<ActivedUserDTO> activedUsers;
    
}
