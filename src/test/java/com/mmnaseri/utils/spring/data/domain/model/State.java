package com.mmnaseri.utils.spring.data.domain.model;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
public class State implements Comparable<State> {

    private String name;
    private String abbreviation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") State that) {
        return this.abbreviation.compareTo(that.abbreviation);
    }

}
