package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.EmbeddedId;

/**
 * @author Balthasar Biedermann
 */
@SuppressWarnings("unused")
public class EntityWithAnnotatedEmbeddedIdField {

    @EmbeddedId
    private EmbeddableId id;

}
