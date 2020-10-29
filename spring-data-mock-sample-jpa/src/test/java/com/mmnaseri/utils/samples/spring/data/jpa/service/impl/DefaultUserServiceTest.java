package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.User;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.GroupRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.MembershipRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.UserRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.GroupService;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.dsl.factory.Start;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:11 PM)
 */
public class DefaultUserServiceTest {

  private DefaultUserService service;
  private UserRepository repository;
  private GroupService groupService;
  private GroupRepository groupRepository;

  @BeforeMethod
  public void setUp() {
    final Start builder = RepositoryFactoryBuilder.builder();
    groupRepository = builder.mock(GroupRepository.class);
    final MembershipRepository membershipRepository = builder.mock(MembershipRepository.class);
    groupService = new DefaultGroupService(groupRepository, membershipRepository);
    repository = builder.mock(UserRepository.class);
    service = new DefaultUserService(repository, groupService);
  }

  @Test
  public void testCreatingAUser() {
    assertThat(repository.count(), is(0L));
    final String username = "milad";
    final String email = "milad@mmnaseri.com";
    final String password = "123456";
    final User user = service.createUser(username, email, password);
    assertThat(user, is(notNullValue()));
    assertThat(user.getId(), is(notNullValue()));
    assertThat(user.getUsername(), is(username));
    assertThat(user.getEmail(), is(email));
    assertThat(user.getPasswordHash(), is(not(password)));
    assertThat(repository.count(), is(1L));
    final User found = repository.findById(user.getId()).orElse(null);
    assertThat(found, is(notNullValue()));
    assertThat(found.getUsername(), is(username));
    assertThat(found.getEmail(), is(email));
    assertThat(found.getPasswordHash(), is(user.getPasswordHash()));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testCreatingADuplicateUser() {
    service.createUser("milad", "email1", "123456");
    service.createUser("milad", "email2", "123456");
  }

  @Test
  public void testLookingUpAUserByEmail() {
    final String id = service.createUser("milad", "milad@domain.com", "123456").getId();
    final User found = service.lookup("MILAD@domain.com");
    assertThat(found, is(notNullValue()));
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testLookingUpAUserByUsername() {
    final String id = service.createUser("milad", "milad@domain.com", "123456").getId();
    final User found = service.lookup("MILAD");
    assertThat(found, is(notNullValue()));
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testLookingForNonExistentUser() {
    final User user = service.lookup("milad");
    assertThat(user, is(nullValue()));
  }

  @Test
  public void testAuthenticatingWithUsername() {
    final String id = service.createUser("milad", "milad@domain.com", "123456").getId();
    final User user = service.authenticate("Milad", "123456");
    assertThat(user, is(notNullValue()));
    assertThat(user.getId(), is(id));
  }

  @Test
  public void testAuthenticatingWithEmail() {
    final String id = service.createUser("milad", "milad@domain.com", "123456").getId();
    final User user = service.authenticate("milad@DOMAIN.com", "123456");
    assertThat(user, is(notNullValue()));
    assertThat(user.getId(), is(id));
  }

  @Test
  public void testAuthenticatingWithWrongHandle() {
    service.createUser("milad", "milad@domain.com", "123456");
    final User user = service.authenticate("milad@DOMAIN", "123456");
    assertThat(user, is(nullValue()));
  }

  @Test
  public void testAuthenticatingWithWrongPassword() {
    service.createUser("milad", "milad@domain.com", "123456");
    final User user = service.authenticate("milad", "987654");
    assertThat(user, is(nullValue()));
  }

  @Test
  public void testDeletingAUser() {
    service.createUser("milad", "milad@mmaseri.com", "123456");
    assertThat(repository.count(), is(1L));
    service.deleteUser("milad");
    assertThat(repository.count(), is(0L));
  }

  @Test
  public void testChangingUserPassword() {
    service.createUser("milad", "milad@mmnaseri.com", "123456");
    assertThat(service.authenticate("milad", "123456"), is(notNullValue()));
    service.updatePassword("milad", "123456", "987654");
    assertThat(service.authenticate("milad", "123456"), is(nullValue()));
    assertThat(service.authenticate("milad", "987654"), is(notNullValue()));
  }

  @Test
  public void testDeletingAUserThatIsPartOfMultipleGroups() {
    final User user = service.createUser("milad", "milad@mmnaseri.com", "123456");
    groupService.join(groupService.createGroup("Group 1"), user);
    groupService.join(groupService.createGroup("Group 2"), user);
    groupService.join(groupService.createGroup("Group 3"), user);
    groupService.join(groupService.createGroup("Group 4"), user);
    assertThat(groupService.groups(user), hasSize(4));
    service.deleteUser(user.getUsername());
    assertThat(groupService.groups(user), is(Matchers.empty()));
    assertThat(groupRepository.count(), is(4L));
  }
}
