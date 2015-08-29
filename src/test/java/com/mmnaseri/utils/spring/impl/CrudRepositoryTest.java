/*
 * Copyright (c) 2014 Milad Naseri
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

package com.mmnaseri.utils.spring.impl;

import com.mmnaseri.utils.spring.KeyGeneration;
import com.mmnaseri.utils.spring.impl.model.Person;
import com.mmnaseri.utils.spring.impl.repository.PersonRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mmnaseri.utils.spring.impl.RepositoryMock.forRepository;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CrudRepositoryTest {

    public static final String NATIONAL_ID_1 = "12345678";
    public static final String NATIONAL_ID_2 = "12345679";
    public static final String NATIONAL_ID_3 = "12345680";
    public static final String NATIONAL_ID_4 = "12345681";
    public static final String ID_1 = "1";
    public static final String ID_2 = "2";
    public static final String ID_3 = "3";
    public static final String ID_4 = "4";
    public static final String NON_ID_5 = "5";

    private PersonRepository getRepository() {
        return forRepository(PersonRepository.class, "id", KeyGeneration.STRING_RANDOM)
                .withData(
                        new Person(ID_1, "Milad", "Naseri", NATIONAL_ID_1),
                        new Person(ID_2, "Ali", "Gigili", NATIONAL_ID_2),
                        new Person(ID_3, "Mammad", "Khatar", NATIONAL_ID_3),
                        new Person(ID_4, "Jumung", "Su", NATIONAL_ID_4)
                )
                .mock();
    }

    @Test
    public void testCreationOfInstance() throws Exception {
        final PersonRepository repository = forRepository(PersonRepository.class, "id", null).mock();
        assertThat(repository, is(notNullValue()));
        assertThat(repository, is(instanceOf(PersonRepository.class)));
    }

    @Test
    public void testFindAll() throws Exception {
        final PersonRepository repository = getRepository();
        final List<String> expectedIds = Arrays.asList(NATIONAL_ID_1, NATIONAL_ID_2, NATIONAL_ID_3, NATIONAL_ID_4);
        final List<Person> all = repository.findAll();
        assertThat(all, hasSize(4));
        for (Person person : all) {
            assertThat(person.getNationalId(), isIn(expectedIds));
        }
    }

    @Test
    public void testFindAllByKeySet() throws Exception {
        final PersonRepository repository = getRepository();
        final Iterable<Person> found = repository.findAll(Arrays.asList(ID_1, ID_2, NON_ID_5));
        assertThat(found, is(notNullValue()));
        final ArrayList<Person> persons = new ArrayList<Person>();
        for (Person person : found) {
            persons.add(person);
        }
        assertThat(persons, hasSize(2));
        final List<String> expectedIds = Arrays.asList(NATIONAL_ID_1, NATIONAL_ID_2);
        for (Person person : persons) {
            assertThat(person.getNationalId(), isIn(expectedIds));
        }
    }

    @Test
    public void testFindOne() throws Exception {
        final PersonRepository repository = getRepository();
        final Person person = repository.findOne(ID_1);
        assertThat(person, is(notNullValue()));
        assertThat(person.getNationalId(), is(NATIONAL_ID_1));
        assertThat(repository.findOne(NON_ID_5), is(nullValue()));
    }

    @Test
    public void testExists() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.exists(ID_1), is(true));
        assertThat(repository.exists(NON_ID_5), is(false));
    }

    @Test
    public void testCount() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.count(), is(4L));
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.count(), is(4L));
        assertThat(repository.findOne(ID_1).getNationalId(), is(NATIONAL_ID_1));
        assertThat(repository.save(new Person(ID_1, null, null, NATIONAL_ID_1 + "x")).getId(), is(ID_1));
        assertThat(repository.count(), is(4L));
        assertThat(repository.findOne(ID_1).getNationalId(), is(NATIONAL_ID_1 + "x"));
        final String id = repository.save(new Person(null, "A", "B", NATIONAL_ID_4 + "A")).getId();
        assertThat(repository.count(), is(5L));
        assertThat(repository.findOne(id), is(notNullValue()));
        assertThat(repository.findOne(id).getNationalId(), is(NATIONAL_ID_4 + "A"));
    }

    @Test
    public void testDeleteByKey() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.count(), is(4L));
        repository.delete(NON_ID_5);
        assertThat(repository.count(), is(4L));
        assertThat(repository.exists(ID_1), is(true));
        repository.delete(ID_1);
        assertThat(repository.exists(ID_1), is(false));
        assertThat(repository.count(), is(3L));
    }

    @Test
    public void testDeleteByEntity() throws Exception {
        final Person nonExistentPerson = new Person(NON_ID_5);
        final Person existingPerson = new Person(ID_1);
        final PersonRepository repository = getRepository();
        assertThat(repository.count(), is(4L));
        repository.delete(nonExistentPerson);
        assertThat(repository.count(), is(4L));
        assertThat(repository.exists(existingPerson.getId()), is(true));
        repository.delete(existingPerson);
        assertThat(repository.exists(existingPerson.getId()), is(false));
        assertThat(repository.count(), is(3L));
    }

    @Test
    public void testDeleteSubSet() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.exists(ID_1), is(true));
        assertThat(repository.exists(ID_2), is(true));
        assertThat(repository.count(), is(4L));
        repository.delete(Arrays.asList(new Person(ID_1), new Person(ID_2), new Person(NON_ID_5)));
        assertThat(repository.exists(ID_1), is(false));
        assertThat(repository.exists(ID_2), is(false));
        assertThat(repository.count(), is(2L));
    }

    @Test
    public void testDeleteAll() throws Exception {
        final PersonRepository repository = getRepository();
        assertThat(repository.count(), is(4L));
        repository.deleteAll();
        assertThat(repository.count(), is(0L));
    }

    @Test
    public void testQueryMethod() throws Exception {
        final PersonRepository repository = getRepository();
        final List<Person> list = repository.findDistinctTop10PeopleByLastNameAndFirstNameLikeOrFirstNameStartsWithOrderByIdDesc("Naseri", "milad", "M");
        assertThat(list, hasSize(2));
        assertThat(list.get(0).getLastName(), is("Khatar"));
        assertThat(list.get(1).getLastName(), is("Naseri"));
    }

    @Test
    public void testCountMethod() throws Exception {
        final PersonRepository repository = getRepository();
        final long count = repository.countByLastNameLike("n");
        assertThat(count, is(1L));
    }

}