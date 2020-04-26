package com.mmnaseri.utils.samples.spring.data.mongo.model;

import org.bson.types.ObjectId;

@SuppressWarnings("unused")
public class Store {

    private ObjectId id;
    private String name;

    public ObjectId getId() {
        return id;
    }

    public Store setId(final ObjectId id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Store setName(final String name) {
        this.name = name;
        return this;
    }

}
