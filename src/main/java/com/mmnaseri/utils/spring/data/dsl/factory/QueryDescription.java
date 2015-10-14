package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface QueryDescription extends DataFunctions {

    DataFunctions extractQueriesUsing(QueryDescriptionExtractor extractor);

}
