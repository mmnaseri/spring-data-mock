package com.mmnaseri.utils.spring.data.sample.models;

import org.springframework.data.annotation.Id;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:52 PM)
 */
@SuppressWarnings("unused")
public class EntityWithMultipleAnnotatedFields {

    @Id
    private String first;

    @Id
    private String second;

}
