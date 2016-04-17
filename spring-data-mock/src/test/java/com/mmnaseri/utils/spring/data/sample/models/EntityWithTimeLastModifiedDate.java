package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 5:20 PM)
 */
public class EntityWithTimeLastModifiedDate {

    private String id;
    @LastModifiedDate
    private Time lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Time getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Time lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
