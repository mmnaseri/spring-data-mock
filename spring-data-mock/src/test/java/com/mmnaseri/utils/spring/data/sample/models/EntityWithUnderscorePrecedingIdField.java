package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:54 PM)
 */
@SuppressWarnings("unused")
public class EntityWithUnderscorePrecedingIdField {

    private String _id;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

}
