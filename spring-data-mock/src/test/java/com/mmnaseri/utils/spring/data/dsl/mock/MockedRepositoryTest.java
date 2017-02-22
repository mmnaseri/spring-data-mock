/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.JpaPersonRepository;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author blackleg
 */
public class MockedRepositoryTest {
    
    private JpaPersonRepository repository;
    
    @BeforeMethod
    public void setUp() throws Exception {
        repository = new RepositoryMockBuilder().useConfiguration(RepositoryFactoryBuilder.defaultConfiguration()).mock(JpaPersonRepository.class);
        assertThat(repository, is(notNullValue()));
    }
    
    
    @Test
    public void testSave() {
        Person person = repository.save(Person.build());
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
        assertThat(repository.findAll(), hasItem(person));
        repository.delete(person);
        assertThat(repository.findAll(), is(empty()));
    }
    
    @Test
    public void testSaveList() {
        List<Person> people = Person.list();
        List<Person> savedPeople = repository.save(people);
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
    }
    
}
