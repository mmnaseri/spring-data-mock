package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
@SuppressWarnings("unused")
public class State implements Comparable<State> {

    private String name;
    private String abbreviation;

    public String getName() {
        return name;
    }

    public State setName(String name) {
        this.name = name;
        return this;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public State setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    @Override
    public int compareTo(State that) {
        return this.abbreviation.compareTo(that.abbreviation);
    }

}
