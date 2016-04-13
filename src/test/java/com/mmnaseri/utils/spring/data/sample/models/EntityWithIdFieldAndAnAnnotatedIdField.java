package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.Id;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:54 PM)
 */
public class EntityWithIdFieldAndAnAnnotatedIdField {

    private String id;
    @Id
    private String annotatedId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnnotatedId() {
        return annotatedId;
    }

    public void setAnnotatedId(String annotatedId) {
        this.annotatedId = annotatedId;
    }

}
