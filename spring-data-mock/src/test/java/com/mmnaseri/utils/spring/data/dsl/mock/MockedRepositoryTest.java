/*
 * Copyright (c) 2016 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
 * MockedRepositoryTest
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
        assertThat(savedPeople, is(notNullValue()));
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
    }

}
