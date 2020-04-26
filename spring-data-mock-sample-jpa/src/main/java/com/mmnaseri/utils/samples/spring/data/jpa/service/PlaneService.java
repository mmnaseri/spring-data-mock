package com.mmnaseri.utils.samples.spring.data.jpa.service;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:39 PM)
 */
@SuppressWarnings("unused")
public interface PlaneService {

    Long create(String model, String serial);

    String lookup(Long id);

    String lookup(String serial);

}
