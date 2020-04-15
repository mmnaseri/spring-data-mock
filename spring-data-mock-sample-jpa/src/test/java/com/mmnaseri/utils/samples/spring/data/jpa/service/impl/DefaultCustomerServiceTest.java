package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CustomerRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CustomerRepositoryExampleSupport;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 2:03 PM)
 */
public class DefaultCustomerServiceTest {

    private DefaultCustomerService service;
    private CustomerRepository repository;

    @BeforeMethod
    public void setUp() throws Exception {
        repository = builder()
                .usingImplementation(CustomerRepositoryExampleSupport.class)
                .mock(CustomerRepository.class);
        service = new DefaultCustomerService(repository);
    }

    @Test
    public void testCustomerRegistration() throws Exception {
        final Date date = date(1988, 0, 1);
        //let's make sure that the database is empty
        assertThat(repository.count(), is(0L));
        final String firstName = "Milad";
        final String lastName = "Naseri";
        //and then register a customer
        final long id = service.register(firstName, lastName, date);
        //after registration, we should have exactly one record
        assertThat(repository.count(), is(1L));
        //and we should be able to load the cutomer by it's ID
        final Customer customer = repository.findById(id).orElse(null);
        //and that customer should be the one we registered
        assertThat(customer, is(notNullValue()));
        assertThat(customer.getId(), is(id));
        assertThat(customer.getFirstName(), is(firstName));
        assertThat(customer.getLastName(), is(lastName));
        assertThat(customer.getBirthday(), is(date));
    }

    @Test
    public void testLoadingCustomerById() throws Exception {
        //let's save a customer to the database first
        final Customer customer = createCustomer("Milad", "Naseri", date(1988, 1, 1));
        //we should be able to locate that via the service
        final Customer loaded = service.findCustomer(customer.getId());
        assertThat(loaded, is(notNullValue()));
        assertThat(loaded.getId(), is(customer.getId()));
        assertThat(loaded.getBirthday(), is(customer.getBirthday()));
        assertThat(loaded.getFirstName(), is(customer.getFirstName()));
        assertThat(loaded.getLastName(), is(customer.getLastName()));
    }

    @Test
    public void testLoadingCustomersByBirthday() throws Exception {
        //let's register three customers, two of which are born within [88/1/1 .. 89/12/28]
        final Customer first = createCustomer("Milad", "Naseri", date(1988, 1, 1));
        final Customer second = createCustomer("Zohreh", "Sadeghi", date(1989, 9, 22));
        createCustomer("Hassan", "Naseri", date(1962, 4, 15));
        //we should be able to look up these customers using the service
        final List<Customer> list = service.findCustomersByBirthday(date(1988, 1, 1), date(1989, 12, 28));
        //and the customers should be the ones indicated above
        assertThat(list, is(notNullValue()));
        assertThat(list, hasSize(2));
        assertThat(list, containsInAnyOrder(first, second));
    }

    @Test
    public void testLoadingCustomersByFirstNameAndLastName() throws Exception {
        //let's save three customers ...
        final Customer customer = createCustomer("Milad", "Naseri", date(1988, 1, 1));
        createCustomer("Zohreh", "Sadeghi", date(1989, 9, 22));
        createCustomer("Hassan", "Naseri", date(1962, 4, 15));
        //... and have the service look up one of them
        final List<Customer> list = service.findCustomersByName("Milad", "Naseri");
        assertThat(list, is(notNullValue()));
        assertThat(list, hasSize(1));
        assertThat(list.get(0), is(customer));
    }

    private Customer createCustomer(String firstName, String lastName, Date birthday) {
        final Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setBirthday(birthday);
        return repository.save(customer);
    }

    private Date date(int year, int month, int day) {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}