package com.mmnaseri.utils.samples.spring.data.jpa.service;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import com.mmnaseri.utils.samples.spring.data.jpa.model.User;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 5:26 PM)
 */
public interface GroupService {

  Group createGroup(String name);

  void deleteGroup(Group group);

  void join(Group group, User user);

  void leave(Group group, User user);

  List<User> members(Group group);

  List<Group> groups(User user);

  void deactivate(Group group, User user);

  List<Group> deactivatedGroups(User user);
}
