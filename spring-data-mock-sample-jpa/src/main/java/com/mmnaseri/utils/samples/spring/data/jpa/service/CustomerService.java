package com.mmnaseri.utils.samples.spring.data.jpa.service;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 1:51 PM)
 */
public interface CustomerService {

    long register(String firstName, String lastName, Date birthday);

    Customer findCustomer(long id);

    List<Customer> findCustomersByBirthday(Date from, Date to);

    List<Customer> findCustomersByName(String firstName, String lastName);

    List<Customer> findCustomersByFirstNames(Collection<String> firstNames);
}
