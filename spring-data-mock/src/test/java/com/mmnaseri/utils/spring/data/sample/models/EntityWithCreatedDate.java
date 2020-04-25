package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:20 PM)
 */
public class EntityWithCreatedDate {

    private String id;
    @CreatedDate
    private Date createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
