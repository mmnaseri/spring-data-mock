package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.InvocationMatcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface QueryDescriptor extends InvocationMatcher {

    RepositoryFactoryConfiguration getConfiguration();

    RepositoryMetadata getRepositoryMetadata();

    boolean isDistinct();

    String getFunction();

    int getLimit();

    Page getPage(Invocation invocation);

    Sort getSort(Invocation invocation);

    List<List<Parameter>> getBranches();

}
