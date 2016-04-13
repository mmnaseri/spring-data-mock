package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractSimpleStringMatcher;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:01 PM)
 */
public class NotMatchingStringMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return false;
    }

}
