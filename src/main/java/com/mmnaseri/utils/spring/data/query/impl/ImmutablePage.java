package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.Page;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class ImmutablePage implements Page {

    private final int pageSize;
    private final int pageNumber;

    public ImmutablePage(int pageSize, int pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

}
