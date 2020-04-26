package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.EmbeddedId;

/**
 * @author Balthasar Biedermann
 */
public class EntityWithAnnotatedEmbeddedIdGetter {

    private EmbeddableId id;

    @EmbeddedId
    private EmbeddableId getId() {
        return id;
    }

    public void setId(EmbeddableId id) {
        this.id = id;
    }

}
