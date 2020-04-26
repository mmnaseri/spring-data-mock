package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/26/15)
 */
@SuppressWarnings("unused")
public class SampleEntity extends BaseEntity {

    private String model;

    public SampleEntity() {
    }

    public SampleEntity(String model) {
        this.model = model;
    }

    public SampleEntity(String id, String model) {
        super(id);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
