package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractBinaryComparableMatcher;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:59 PM)
 */
public class NotMatchingBinaryComparableMatcher extends AbstractBinaryComparableMatcher {

    @Override
    protected boolean matches(Comparable value, Comparable first, Comparable second) {
        return false;
    }

}
