package com.opisvn.kanhome.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

/**
 * A Topic.
 */

@Document(collection = "appVersion")
@Getter
@Setter
public class AppVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("updated_date")
    private Date updatedDate;

    @Field("android_version")
    private Date androidVersion;
    
    @Field("ios_version")
    private Date iosVersion;
}
