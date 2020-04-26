package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.EmbeddedId;

/**
 * @author Balthasar Biedermann
 */
public class EntityWithAnnotatedEmbeddedIdFieldFromJPA {

    @EmbeddedId
    private EmbeddableId customIdProperty;

    public EmbeddableId getCustomIdProperty() {
        return customIdProperty;
    }

    public void setCustomIdProperty(EmbeddableId customIdProperty) {
        this.customIdProperty = customIdProperty;
    }


}
