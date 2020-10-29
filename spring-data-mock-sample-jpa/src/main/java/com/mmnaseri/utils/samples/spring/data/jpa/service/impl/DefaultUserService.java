package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import com.mmnaseri.utils.samples.spring.data.jpa.model.User;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.UserRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.GroupService;
import com.mmnaseri.utils.samples.spring.data.jpa.service.UserService;
import com.mmnaseri.utils.samples.spring.data.jpa.utils.EncryptionUtils;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:09 PM)
 */
public class DefaultUserService implements UserService {

  private final UserRepository repository;
  private final GroupService groupService;

  public DefaultUserService(UserRepository repository, GroupService groupService) {
    this.repository = repository;
    this.groupService = groupService;
  }

  @Override
  public User createUser(String username, String email, String password) {
    if (repository.findByUsernameIgnoreCase(username) != null
        || repository.findByEmailIgnoreCase(email) != null) {
      throw new IllegalArgumentException();
    }
    final User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPasswordHash(EncryptionUtils.encrypt(password));
    return repository.save(user);
  }

  @Override
  public void updatePassword(String handle, String oldPassword, String newPassword) {
    final User user = authenticate(handle, oldPassword);
    if (user == null) {
      throw new IllegalStateException();
    }
    user.setPasswordHash(EncryptionUtils.encrypt(newPassword));
    repository.save(user);
  }

  @Override
  public void deleteUser(String handle) {
    final User user = lookup(handle);
    if (user == null) {
      throw new IllegalStateException();
    }
    final List<Group> groups = groupService.groups(user);
    for (Group group : groups) {
      groupService.leave(group, user);
    }
    repository.delete(user);
  }

  @Override
  public User lookup(String handle) {
    return repository.findByUsernameOrEmailAllIgnoreCase(handle, handle);
  }

  @Override
  public User authenticate(String handle, String password) {
    final User user = lookup(handle);
    if (user == null) {
      return null;
    }
    if (user.getPasswordHash().equals(EncryptionUtils.encrypt(password))) {
      return user;
    }
    return null;
  }
}
