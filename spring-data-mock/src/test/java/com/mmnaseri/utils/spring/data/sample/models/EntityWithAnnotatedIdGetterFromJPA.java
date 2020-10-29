package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.Id;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/8/16, 1:37 AM)
 */
@SuppressWarnings("unused")
public class EntityWithAnnotatedIdGetterFromJPA {

    private Integer myCustomId;

    @Id
    public Integer getMyCustomId() {
        return myCustomId;
    }

    public void setMyCustomId(Integer myCustomId) {
        this.myCustomId = myCustomId;
    }

}
