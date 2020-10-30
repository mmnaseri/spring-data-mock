package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:05 PM)
 */
public interface UserRepository extends JpaRepository<User, String> {

  User findByUsernameOrEmailAllIgnoreCase(String username, String email);

  User findByUsernameIgnoreCase(String username);

  User findByEmailIgnoreCase(String email);
}
