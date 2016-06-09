package com.mmnaseri.utils.spring.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/8/16, 11:50 AM)
 */
public abstract class PagingAndSortingSupport {

    public static Page page(Collection entries, Pageable pageable) {
        return PagingAndSortingUtils.page(entries, pageable);
    }

}
