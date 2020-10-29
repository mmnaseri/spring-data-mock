package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import java.util.Collection;

/**
 * This matcher checks to see if the argument being passed (the collection) contains the value on the object.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsInMatcher extends AbstractCollectionMatcher {

    @Override
    protected boolean matches(Object actual, Collection collection) {
        return actual != null && collection.contains(actual);
    }

}
