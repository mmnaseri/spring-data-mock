package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CustomerRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 2:03 PM)
 */
public class DefaultCustomerServiceTest {

    private DefaultCustomerService service;
    private CustomerRepository repository;

    @BeforeMethod
    public void setUp() throws Exception {
        repository = builder().mock(CustomerRepository.class);
        service = new DefaultCustomerService(repository);
    }

    @Test
    public void testCustomerRegistration() throws Exception {
        final Date date = getDate(1988, 0, 1);
        assertThat(repository.count(), is(0L));
        final String firstName = "Milad";
        final String lastName = "Naseri";
        final long id = service.register(firstName, lastName, date);
        assertThat(repository.count(), is(1L));
        final Customer customer = repository.findOne(id);
        assertThat(customer, is(notNullValue()));
        assertThat(customer.getId(), is(id));
        assertThat(customer.getFirstName(), is(firstName));
        assertThat(customer.getLastName(), is(lastName));
        assertThat(customer.getBirthday(), is(date));
    }

    private Date getDate(int year, int month, int day) {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DATE, day);
        return calendar.getTime();
    }

}