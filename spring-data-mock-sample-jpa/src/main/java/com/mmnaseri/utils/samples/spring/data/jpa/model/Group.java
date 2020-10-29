package com.mmnaseri.utils.samples.spring.data.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:03 PM)
 */
@SuppressWarnings("unused")
@Entity
public class Group {

    @Id
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
