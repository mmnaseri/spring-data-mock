package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:21 PM)
 */
public class EntityWithDateTimeLastModifiedDate {

    private String id;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
