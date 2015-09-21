package com.mmnaseri.utils.spring.data.domain;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface RepositoryMetadata {

    Class<? extends Serializable> getIdentifierType();

    Class<?> getEntityType();

    Class<?> getRepositoryInterface();

}
