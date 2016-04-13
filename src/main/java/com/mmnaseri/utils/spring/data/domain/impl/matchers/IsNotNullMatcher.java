package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This matcher will determine if the value on the object is not {@literal null}
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class IsNotNullMatcher extends AbstractStateMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value) {
        return value != null;
    }

}
