package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultQueryDescriptorTest {

  private OperatorContext operatorContext;

  @BeforeMethod
  public void setUp() {
    operatorContext = new DefaultOperatorContext();
  }

  @Test
  public void testGettingPageWhenPageExtractorIsNull() {
    final DefaultQueryDescriptor descriptor =
        new DefaultQueryDescriptor(false, null, 0, null, null, null, null, null);
    assertThat(descriptor.getPage(new ImmutableInvocation(null, new Object[] {})), is(nullValue()));
  }

  @Test
  public void testGettingSortWhenSortExtractorIsNull() {
    final DefaultQueryDescriptor descriptor =
        new DefaultQueryDescriptor(false, null, 0, null, null, null, null, null);
    assertThat(descriptor.getSort(new ImmutableInvocation(null, new Object[] {})), is(nullValue()));
  }

  @Test
  public void testMatchingWhenThereAreNotAnyConditions() {
    final List<List<Parameter>> branches = Collections.emptyList();
    final DefaultQueryDescriptor descriptor =
        new DefaultQueryDescriptor(false, null, 0, null, null, branches, null, null);
    assertThat(descriptor.matches(null, new ImmutableInvocation(null, null)), is(false));
    assertThat(descriptor.matches(new Object(), new ImmutableInvocation(null, null)), is(true));
  }

  @Test
  public void testThatEachBranchIsConjunctive() {
    final List<List<Parameter>> branches = new ArrayList<>();
    final ArrayList<Parameter> branch = new ArrayList<>();
    branches.add(branch);
    // getByFirstNameAndLastName(:0, :1)
    branch.add(
        new ImmutableParameter(
            "firstName",
            Collections.emptySet(),
            new int[] {0},
            operatorContext.getBySuffix("Equals")));
    branch.add(
        new ImmutableParameter(
            "lastName",
            Collections.emptySet(),
            new int[] {1},
            operatorContext.getBySuffix("IsNot")));
    final DefaultQueryDescriptor descriptor =
        new DefaultQueryDescriptor(false, null, 0, null, null, branches, null, null);
    final Person person = new Person().setFirstName("X").setLastName("Y");
    assertThat(
        descriptor.matches(person, new ImmutableInvocation(null, new Object[] {"A", "Y"})),
        is(false));
    assertThat(
        descriptor.matches(person, new ImmutableInvocation(null, new Object[] {"X", "Y"})),
        is(false));
    assertThat(
        descriptor.matches(person, new ImmutableInvocation(null, new Object[] {"A", "B"})),
        is(false));
    assertThat(
        descriptor.matches(person, new ImmutableInvocation(null, new Object[] {"X", "B"})),
        is(true));
  }

  @Test
  public void testThatBranchesAreDisjunctive() {
    final List<List<Parameter>> branches = new ArrayList<>();
    final ArrayList<Parameter> first = new ArrayList<>();
    final ArrayList<Parameter> second = new ArrayList<>();
    final ArrayList<Parameter> third = new ArrayList<>();
    branches.add(first);
    branches.add(second);
    branches.add(third);
    // getByFirstNameAndLastNameOrIdOrAddressCityAndAddressStreet(:0, :1, :2, :3, :4)
    first.add(
        new ImmutableParameter(
            "firstName",
            Collections.emptySet(),
            new int[] {0},
            operatorContext.getBySuffix("Equals")));
    first.add(
        new ImmutableParameter(
            "lastName",
            Collections.emptySet(),
            new int[] {1},
            operatorContext.getBySuffix("Equals")));
    second.add(
        new ImmutableParameter(
            "id", Collections.emptySet(), new int[] {2}, operatorContext.getBySuffix("Equals")));
    third.add(
        new ImmutableParameter(
            "address.city",
            Collections.emptySet(),
            new int[] {3},
            operatorContext.getBySuffix("Equals")));
    third.add(
        new ImmutableParameter(
            "address.street",
            Collections.emptySet(),
            new int[] {4},
            operatorContext.getBySuffix("Equals")));
    final DefaultQueryDescriptor descriptor =
        new DefaultQueryDescriptor(false, null, 0, null, null, branches, null, null);
    final Person person =
        new Person()
            .setId("1")
            .setFirstName("X")
            .setLastName("Y")
            .setAddress(new Address().setCity("Shiraz").setStreet("Chaharbagh"));
    assertThat(
        descriptor.matches(
            person, new ImmutableInvocation(null, new Object[] {"X", "Y", "2", "Mashad", "Reza"})),
        is(true)); // first branch
    assertThat(
        descriptor.matches(
            person, new ImmutableInvocation(null, new Object[] {"A", "B", "1", "Mashad", "Reza"})),
        is(true)); // second branch
    assertThat(
        descriptor.matches(
            person,
            new ImmutableInvocation(null, new Object[] {"A", "B", "3", "Shiraz", "Chaharbagh"})),
        is(true)); // third branch
    assertThat(
        descriptor.matches(
            person,
            new ImmutableInvocation(null, new Object[] {"A", "B", "3", "Tehran", "Tajrish"})),
        is(false)); // none
  }

  @Test
  public void testToString() {
    final QueryDescriptor noFunctionNotDistinct =
        new DefaultQueryDescriptor(false, null, 0, null, null, Collections.emptyList(), null, null);
    assertThat(noFunctionNotDistinct.toString(), is("[]"));
    final QueryDescriptor functionNotDistinct =
        new DefaultQueryDescriptor(
            false, "xyz", 0, null, null, Collections.emptyList(), null, null);
    assertThat(functionNotDistinct.toString(), is("xyz []"));
    final QueryDescriptor functionDistinct =
        new DefaultQueryDescriptor(true, "xyz", 0, null, null, Collections.emptyList(), null, null);
    assertThat(functionDistinct.toString(), is("xyz distinct []"));
  }
}
