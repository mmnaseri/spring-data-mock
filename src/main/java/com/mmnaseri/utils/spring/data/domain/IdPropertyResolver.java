package com.mmnaseri.utils.spring.data.domain;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public interface IdPropertyResolver {

    String resolve(Class<?> entityType, Class<? extends Serializable> idType);

}
