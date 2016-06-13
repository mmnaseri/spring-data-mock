package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;
import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import org.springframework.data.domain.Example;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 5:30 PM)
 */
public class CustomerRepositoryExampleSupport implements RepositoryAware<CustomerRepository> {

    private CustomerRepository repository;

    public List<Customer> findByExample(Example<Customer> example) {
        return repository.findAll(example);
    }

    @Override
    public void setRepository(CustomerRepository repository) {
        this.repository = repository;
    }

}
