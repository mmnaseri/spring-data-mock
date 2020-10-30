package com.mmnaseri.utils.samples.spring.data.jpa.service;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import com.mmnaseri.utils.samples.spring.data.jpa.model.User;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:06 PM)
 */
@SuppressWarnings("unused")
public interface UserService {

  User createUser(String username, String email, String password);

  void updatePassword(String handle, String oldPassword, String newPassword);

  void deleteUser(String handle);

  User lookup(String handle);

  User authenticate(String handle, String password);

  List<Group> deactivatedGroups(User user);
}
