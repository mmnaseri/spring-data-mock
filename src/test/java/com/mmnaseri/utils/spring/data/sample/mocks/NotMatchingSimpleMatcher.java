package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractSimpleMatcher;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 7:00 PM)
 */
public class NotMatchingSimpleMatcher extends AbstractSimpleMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object actual, Object expected) {
        return false;
    }

}
