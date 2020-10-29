package com.mmnaseri.utils.samples.spring.data.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:04 PM)
 */
@Entity
@SuppressWarnings("unused")
public class Membership {

    @Id
    private String id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Group group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
