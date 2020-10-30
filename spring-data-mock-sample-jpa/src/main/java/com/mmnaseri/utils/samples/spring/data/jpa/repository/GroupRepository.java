package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:05 PM)
 */
public interface GroupRepository extends JpaRepository<Group, String> {}
