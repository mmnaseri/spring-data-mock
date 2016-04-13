package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:20 PM)
 */
public class EntityWithTimestampDateLastModifiedDate {

    private String id;
    @LastModifiedDate
    private Timestamp lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
