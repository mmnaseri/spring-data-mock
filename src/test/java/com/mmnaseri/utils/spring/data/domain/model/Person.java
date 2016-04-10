package com.mmnaseri.utils.spring.data.domain.model;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
public class Person {

    private String id;
    private String firstName;
    private String lastName;
    private Zip addressZip;
    private Address address;

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

}
