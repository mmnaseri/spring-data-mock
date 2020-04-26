package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
public class Address {

    private String city;
    private String street;
    private State state;
    private Zip zip;

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public State getState() {
        return state;
    }

    public Address setState(State state) {
        this.state = state;
        return this;
    }

    public Zip getZip() {
        return zip;
    }

    public Address setZip(Zip zip) {
        this.zip = zip;
        return this;
    }

}
