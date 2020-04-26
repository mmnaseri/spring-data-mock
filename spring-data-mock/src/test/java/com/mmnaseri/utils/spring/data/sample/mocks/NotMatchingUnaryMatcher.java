package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractUnaryMatcher;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:01 PM)
 */
public class NotMatchingUnaryMatcher extends AbstractUnaryMatcher {

    @Override
    protected boolean matches(Object value) {
        return false;
    }

}
