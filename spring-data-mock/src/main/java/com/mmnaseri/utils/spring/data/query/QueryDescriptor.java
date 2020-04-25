package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.InvocationMatcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

import java.util.List;

/**
 * This interface encapsulates everything that we can know about a query.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface QueryDescriptor extends InvocationMatcher {

    /**
     * @return the repository factory configuration that were used when the current query was discovered
     */
    RepositoryFactoryConfiguration getConfiguration();

    /**
     * @return metadata pertaining to the repository to which this query belongs
     */
    RepositoryMetadata getRepositoryMetadata();

    /**
     * @return whether query results are expected to be distinct
     */
    boolean isDistinct();

    /**
     * @return the function name that should be applied to the entities selected as per this query. Could be {@literal
     * null} to indicate that no function should be applied.
     */
    String getFunction();

    /**
     * @return the maximum number of items this query is expected to return. A non-positive value indicates no limit.
     */
    int getLimit();

    /**
     * Given an actual, runtime invocation, returns the page metadata for that invocation as per this query, or
     * {@literal null} if no pagination was indicated.
     *
     * @param invocation the invocation
     * @return the pagination metadata
     */
    Page getPage(Invocation invocation);

    /**
     * Given an actual, runtime invocation, returns the sort metadata for that invocation as per this qery, or returns
     * {@literal null} to indicate no ordering is required.
     *
     * @param invocation the invocation
     * @return the sort metadata
     */
    Sort getSort(Invocation invocation);

    /**
     * @return a list of decision branches which forms this query. Each decision branch is a set of conjunctive criteria
     * about any single entity. This means to be evaluated to {@literal true}, all of the parameters in that branch must
     * match the entity. Decision branches are disjunctive towards each other, meaning that the moment one of them was
     * evaluated to {@literal true} the rest will be disregarded.
     */
    List<List<Parameter>> getBranches();

}
