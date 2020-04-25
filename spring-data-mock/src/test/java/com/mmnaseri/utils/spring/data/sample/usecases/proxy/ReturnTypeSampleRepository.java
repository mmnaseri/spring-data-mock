package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.GeoPage;
import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:31 PM)
 */
public interface ReturnTypeSampleRepository {

    Object findOther();

    Set findSet();

    CustomCollectionImplementation findCustomCollection();

    Future findFuture();

    GeoPage findGeoPage();

    Iterator findIterator();

    Iterable findIterable();

    ListenableFuture findListenableFuture();

    List findList();

    int findPrimitive();

    Queue findQueue();

    LinkedList findLinkedList();

    Slice findSlice();

    Page findPage();

    int findInt();

    long findLong();

    short findShort();

    double findDouble();

    float findFloat();

    byte findByte();

    BigDecimal findBigDecimal();

    Integer findInteger();

    void doSomething();

    Person findPerson();

}
