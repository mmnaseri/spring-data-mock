package com.mmnaseri.utils.spring.data.sample.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/21/15)
 */
public class Person {
    
    private String id;
    private String firstName;
    private String lastName;
    private Zip addressZip;
    private Address address;
    private Integer age;

    public String getId() {
        return id;
    }

    public Person setId(String id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Zip getAddressZip() {
        return addressZip;
    }

    public Person setAddressZip(Zip addressZip) {
        this.addressZip = addressZip;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Person setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Person setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id != null ? id.equals(person.id) : person.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    public static Person build() {
        return new Person();
    }

    public static List<Person> list(int count) {
        ArrayList<Person> people = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            people.add(Person.build());
        }
        return people;
    }
    
    public static List<Person> list() {
        return list(1);
    }

}
