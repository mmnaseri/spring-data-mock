package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.InvocationMatcher;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface QueryDescriptor extends InvocationMatcher {

    RepositoryMetadata getRepositoryMetadata();

    boolean isDistinct();

    String getFunction();

    int getLimit();

    Page getPage(Invocation invocation);

    Sort getSort(Invocation invocation);

}
