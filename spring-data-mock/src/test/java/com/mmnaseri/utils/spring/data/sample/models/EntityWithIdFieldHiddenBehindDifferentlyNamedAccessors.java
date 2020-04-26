package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:56 PM)
 */
public class EntityWithIdFieldHiddenBehindDifferentlyNamedAccessors {

    private String id;

    public String getIdentifier() {
        return id;
    }

    public void setIdentifier(String id) {
        this.id = id;
    }

}
