package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.Id;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:53 PM)
 */
public class EntityWithMultipleAnnotatedIdGetters {

    private String first;
    private String second;

    @Id
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @Id
    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
