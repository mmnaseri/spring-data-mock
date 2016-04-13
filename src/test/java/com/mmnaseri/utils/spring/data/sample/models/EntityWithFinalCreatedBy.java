package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.CreatedBy;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:21 PM)
 */
public class EntityWithFinalCreatedBy {

    private String id;
    @CreatedBy
    private final String createdBy = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
