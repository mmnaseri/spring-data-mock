package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractBinaryMatcher;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:59 PM)
 */
public class NotMatchingBinaryMatcher extends AbstractBinaryMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value, Object first, Object second) {
        return false;
    }

}
