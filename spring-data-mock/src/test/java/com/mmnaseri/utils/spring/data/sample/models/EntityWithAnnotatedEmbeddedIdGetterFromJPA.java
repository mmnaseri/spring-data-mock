package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.EmbeddedId;

/**
 * @author Balthasar Biedermann
 */
public class EntityWithAnnotatedEmbeddedIdGetterFromJPA {

    private EmbeddableId myCustomId;

    @EmbeddedId
    private EmbeddableId getMyCustomId() {
        return myCustomId;
    }

    public void setMyCustomId(EmbeddableId myCustomId) {
        this.myCustomId = myCustomId;
    }

}
