package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractStateMatcher;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:01 PM)
 */
public class NotMatchingStateMatcher extends AbstractStateMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value) {
        return false;
    }

}
