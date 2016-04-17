package com.mmnaseri.utils.spring.data.domain.impl;

/**
 * This class contains a query's modifiers: the limit put on the number of results, and whether or not the
 * values should be distinct.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/13/16, 9:25 AM)
 */
class QueryModifiers {

    private final int limit;
    private final boolean distinct;

    QueryModifiers(int limit, boolean distinct) {
        this.limit = limit;
        this.distinct = distinct;
    }

    int getLimit() {
        return limit;
    }

    boolean isDistinct() {
        return distinct;
    }

}
