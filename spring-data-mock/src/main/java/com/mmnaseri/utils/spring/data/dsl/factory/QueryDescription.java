package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;

/**
 * This interface lets us define the query description extractor
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface QueryDescription extends DataFunctions {

    /**
     * Tells the builder to use the given query description extractor instead of the default
     *
     * @param extractor the extractor
     * @return the rest of the configuration
     */
    DataFunctions extractQueriesUsing(MethodQueryDescriptionExtractor extractor);

}
