package com.mmnaseri.utils.samples.spring.data.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:34 PM)
 */
@Entity
public class Plane extends SerialAwareEntity {

    @Id
    private Long id;
    private String model;
    private int capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
