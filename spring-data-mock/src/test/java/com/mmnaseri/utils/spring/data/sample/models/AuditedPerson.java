package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 11:43 AM)
 */
@SuppressWarnings("unused")
public class AuditedPerson extends Person {

    @LastModifiedBy
    private String modifiedBy;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Date modifiedDate;
    @CreatedDate
    private Date createdDate;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public AuditedPerson setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AuditedPerson setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public AuditedPerson setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public AuditedPerson setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

}
