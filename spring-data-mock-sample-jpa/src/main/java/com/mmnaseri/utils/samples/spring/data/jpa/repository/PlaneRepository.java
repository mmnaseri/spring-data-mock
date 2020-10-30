package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:34 PM)
 */
public interface PlaneRepository
    extends JpaRepository<Plane, Long>, SerialAwareEntityRepository<Plane> {}
