package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:54 PM)
 */
public class EntityWithUnderscorePrecedingIdField {

    private String _id;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

}
