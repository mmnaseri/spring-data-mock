package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.CreatedBy;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:21 PM)
 */
public class EntityWithWriteOnlyCreatedBy {

    private String id;

    @CreatedBy
    private String createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
