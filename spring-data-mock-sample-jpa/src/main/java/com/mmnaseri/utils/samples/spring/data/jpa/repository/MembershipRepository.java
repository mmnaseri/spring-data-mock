package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import com.mmnaseri.utils.samples.spring.data.jpa.model.Membership;
import com.mmnaseri.utils.samples.spring.data.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:06 PM)
 */
public interface MembershipRepository extends JpaRepository<Membership, String> {

  List<Membership> findByUser(User user);

  List<Membership> findByGroup(Group group);

  Membership findByUserAndGroup(User user, Group group);

  List<Membership> findAllByUserAndActive(User user, boolean active);
}
