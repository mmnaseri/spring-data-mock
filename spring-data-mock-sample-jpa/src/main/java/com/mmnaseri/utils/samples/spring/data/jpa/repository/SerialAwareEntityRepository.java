package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.SerialAwareEntity;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:37 PM)
 */
public interface SerialAwareEntityRepository<E extends SerialAwareEntity> {

  E lookupBySerial(String serial);
}
