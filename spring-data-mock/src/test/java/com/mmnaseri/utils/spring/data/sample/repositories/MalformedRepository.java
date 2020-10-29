package com.mmnaseri.utils.spring.data.sample.repositories;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:07 PM)
 */
@SuppressWarnings("unused")
public interface MalformedRepository {

    void Malformed();

    void findFirst5First10();

    void findFirstFirst10();

    void findTop10Top5();

    void findDistinctDistinct();

    void findTop10Distinct();

    void findTop10DistinctBy();

    void findByUnknownProperty();

    void findByFirstName();

    void findByFirstNameAnd(String firstName);

    void findByFirstNameOr(String firstName);

    void findByFirstName(Object name);

    void findByFirstName(String name, Object extra);

    void findByFirstName(String name, Object first, Object second);

    void findByFirstNameOrderBy(String firstName);

    void findByFirstNameOrderByAddressAsc(String firstName);

    void findByFirstNameOrderByXyzDesc(String firstName);

    void findByFirstNameOrderByFirstNameAsc(String firstName, org.springframework.data.domain.Sort sort);

}
