package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:52 PM)
 */
public class EntityWithoutAnnotatedIdGetter {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
