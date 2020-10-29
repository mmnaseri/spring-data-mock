package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Plane;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.PlaneRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.PlaneService;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:39 PM)
 */
public class DefaultPlaneService implements PlaneService {

    private final PlaneRepository repository;

    public DefaultPlaneService(PlaneRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long create(String model, String serial) {
        final Plane plane = new Plane();
        plane.setModel(model);
        plane.setCapacity(100);
        plane.setSerial(serial);
        return repository.save(plane).getId();
    }

    @Override
    public String lookup(Long id) {
        return repository.findById(id).map(Plane::getModel).orElse(null);
    }

    @Override
    public String lookup(String serial) {
        final Plane plane = repository.lookupBySerial(serial);
        if (plane == null) {
            return null;
        }
        return plane.getModel();
    }

}
