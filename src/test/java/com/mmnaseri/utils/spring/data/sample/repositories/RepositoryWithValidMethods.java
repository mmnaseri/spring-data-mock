package com.mmnaseri.utils.spring.data.sample.repositories;

import org.springframework.data.domain.Pageable;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 7:08 PM)
 */
@SuppressWarnings("unused")
public interface RepositoryWithValidMethods {

    void find();

    void test();

    void findByFirstNameAndLastNameEquals(String firstName, String lastName);

    void findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);

    void findByFirstNameOrderByLastNameDescAgeAsc(String firstName);

    void findByFirstNameOrLastName(String firstName, String lastName);

    void findByFirstNameOrderByLastNameDesc(String firstName, Pageable pageable);

    void findByFirstName(String firstName, Pageable pageable);

    void findByFirstName(String firstName, org.springframework.data.domain.Sort sort);

    void findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);

    void myFunctionByFirstName(String firstName);

    void findByFirstNameAndLastNameOrAddressCityOrAgeGreaterThan(String firstName, String lastName, String city, Integer age);

    void findAll();

    void findAll(Pageable pageable);

}
