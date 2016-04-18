package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractCollectionMatcher;

import java.util.Collection;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:59 PM)
 */
public class SpyingCollectionMatcher extends AbstractCollectionMatcher {

    private Collection collection;

    @Override
    protected boolean matches(Parameter parameter, Object actual, Collection collection) {
        this.collection = collection;
        return false;
    }

    public Collection<?> getCollection() {
        return collection;
    }

}
