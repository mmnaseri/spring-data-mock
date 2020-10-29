package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:56 PM)
 */
@SuppressWarnings("unused")
public class EntityWithNoImmediatelyResolvableIdProperty {

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
