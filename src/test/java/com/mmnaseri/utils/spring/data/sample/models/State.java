package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/21/15)
 */
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
    public int compareTo(@SuppressWarnings("NullableProblems") State that) {
        return this.abbreviation.compareTo(that.abbreviation);
    }

}
