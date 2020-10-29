package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:20 PM)
 */
@SuppressWarnings("unused")
public class EntityWithSqlDateLastModifiedDate {

    private String id;
    @LastModifiedDate
    private java.sql.Date lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.sql.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(java.sql.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
