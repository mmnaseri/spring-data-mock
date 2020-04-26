package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Group;
import com.mmnaseri.utils.samples.spring.data.jpa.model.Membership;
import com.mmnaseri.utils.samples.spring.data.jpa.model.User;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.GroupRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.MembershipRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.UserRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.GroupService;
import com.mmnaseri.utils.samples.spring.data.jpa.service.UserService;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.dsl.factory.Start;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/30/16, 9:15 AM)
 */
public class DefaultGroupServiceTest {

    private GroupService service;
    private GroupRepository groupRepository;
    private UserService userService;
    private MembershipRepository membershipRepository;

    @BeforeMethod
    public void setUp() throws Exception {
        final Start builder = RepositoryFactoryBuilder.builder();
        groupRepository = builder.mock(GroupRepository.class);
        membershipRepository = builder.mock(MembershipRepository.class);
        final UserRepository userRepository = builder.mock(UserRepository.class);
        service = new DefaultGroupService(groupRepository, membershipRepository);
        userService = new DefaultUserService(userRepository, service);
    }

    @Test
    public void testCreatingAGroup() throws Exception {
        assertThat(groupRepository.count(), is(0L));
        final String name = "My Group";
        final Group group = service.createGroup(name);
        assertThat(group, is(notNullValue()));
        assertThat(group.getName(), is(name));
        assertThat(groupRepository.count(), is(1L));
        final Group found = groupRepository.findById(group.getId()).orElse(null);
        assertThat(found, is(notNullValue()));
        assertThat(found.getName(), is(name));
    }

    @Test
    public void testDeletingAnEmptyGroup() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        service.deleteGroup(group);
        assertThat(groupRepository.count(), is(0L));
    }

    @Test
    public void testEstablishingMembership() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        final User user = userService.createUser("milad", "milad@domain.com", "123456");
        service.join(group, user);
        assertThat(membershipRepository.count(), is(1L));
        final Membership membership = membershipRepository.findAll().get(0);
        assertThat(membership, is(notNullValue()));
        assertThat(membership.getGroup(), is(notNullValue()));
        assertThat(membership.getGroup().getId(), is(group.getId()));
        assertThat(membership.getUser(), is(notNullValue()));
        assertThat(membership.getUser().getId(), is(user.getId()));
    }

    @Test
    public void testBreakingAMembership() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        final User user = userService.createUser("milad", "milad@domain.com", "123456");
        final Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        membershipRepository.save(membership);
        service.leave(group, user);
        assertThat(membershipRepository.count(), is(0L));
    }

    @Test
    public void testListingGroupMembers() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        final User user = userService.createUser("milad", "milad@domain.com", "123456");
        final Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        membershipRepository.save(membership);
        final List<User> users = service.members(group);
        assertThat(users, is(notNullValue()));
        assertThat(users, hasSize(1));
        assertThat(users.get(0), is(notNullValue()));
        assertThat(users.get(0).getId(), is(user.getId()));
    }

    @Test
    public void testListingUserGroups() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        final User user = userService.createUser("milad", "milad@domain.com", "123456");
        final Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        membershipRepository.save(membership);
        final List<Group> groups = service.groups(user);
        assertThat(groups, is(notNullValue()));
        assertThat(groups, hasSize(1));
        assertThat(groups.get(0), is(notNullValue()));
        assertThat(groups.get(0).getId(), is(group.getId()));
    }

    @Test
    public void testDeletingAGroupWithMembers() throws Exception {
        Group group = new Group();
        group.setName("My Group");
        group = groupRepository.save(group);
        final User user = userService.createUser("milad", "milad@domain.com", "123456");
        final Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        membershipRepository.save(membership);
        service.deleteGroup(group);
        assertThat(groupRepository.count(), is(0L));
        assertThat(membershipRepository.count(), is(0L));
    }

}
