package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import com.mmnaseri.utils.spring.data.query.impl.ImmutableOrder;
import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.ChildZip;
import com.mmnaseri.utils.spring.data.sample.models.OtherChildZip;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.models.Zip;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16)
 */
public class PropertyComparatorTest {

  @Test(expectedExceptions = InvalidArgumentException.class)
  public void testWhenFirstValueDoesNotExist() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address.city", NullHandling.DEFAULT));
    //noinspection ResultOfMethodCallIgnored
    comparator.compare(new Address(), new Person());
  }

  @Test(expectedExceptions = InvalidArgumentException.class)
  public void testWhenSecondValueDoesNotExist() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address.city", NullHandling.DEFAULT));
    //noinspection ResultOfMethodCallIgnored
    comparator.compare(new Person(), new Address());
  }

  @Test
  public void testNullFirst() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address.city", NullHandling.NULLS_FIRST));
    assertThat(
        comparator.compare(new Person().setAddress(new Address().setCity("A")), new Person()),
        is(1));
    assertThat(
        comparator.compare(new Person(), new Person().setAddress(new Address().setCity("A"))),
        is(-1));
  }

  @Test
  public void testNullLast() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address.city", NullHandling.NULLS_LAST));
    assertThat(
        comparator.compare(new Person().setAddress(new Address().setCity("A")), new Person()),
        is(-1));
    assertThat(
        comparator.compare(new Person(), new Person().setAddress(new Address().setCity("A"))),
        is(1));
  }

  @Test
  public void testBothAreNull() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address.city", NullHandling.NULLS_LAST));
    assertThat(comparator.compare(new Person(), new Person()), is(0));
  }

  @Test(expectedExceptions = InvalidArgumentException.class)
  public void testWhenValuesAreNotComparable() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "address", NullHandling.NULLS_LAST));
    //noinspection ResultOfMethodCallIgnored
    comparator.compare(
        new Person().setAddress(new Address()), new Person().setAddress(new Address()));
  }

  @Test
  public void testWhenSecondIsSubTypeOfFirst() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "addressZip", NullHandling.NULLS_FIRST));
    final int comparison =
        comparator.compare(
            new Person().setAddressZip(new Zip().setPrefix("a")),
            new Person().setAddressZip(new ChildZip().setPrefix("b")));
    assertThat(comparison, is(-1));
  }

  @Test
  public void testWhenFirstIsSubTypeOfSecond() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "addressZip", NullHandling.NULLS_FIRST));
    final int comparison =
        comparator.compare(
            new Person().setAddressZip(new ChildZip().setPrefix("b")),
            new Person().setAddressZip(new Zip().setPrefix("a")));
    assertThat(comparison, is(1));
  }

  @Test(expectedExceptions = InvalidArgumentException.class)
  public void testWhenValuesAreNotOfTheSameType() {
    final PropertyComparator comparator =
        new PropertyComparator(
            new ImmutableOrder(SortDirection.ASCENDING, "addressZip", NullHandling.NULLS_FIRST));
    //noinspection ResultOfMethodCallIgnored
    comparator.compare(
        new Person().setAddressZip(new ChildZip().setPrefix("b")),
        new Person().setAddressZip(new OtherChildZip().setPrefix("a")));
  }
}
