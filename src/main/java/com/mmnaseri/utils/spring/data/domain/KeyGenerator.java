package com.mmnaseri.utils.spring.data.domain;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface KeyGenerator<S extends Serializable> {

    S generate();

}
