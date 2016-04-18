package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This matcher will return {@literal true} if the argument passed is not equal to the value on the object, even
 * when their case differences are ignored.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class IsNotLikeMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return (actual == null && argument != null) || (actual != null && argument == null) || (actual != null && !actual.equalsIgnoreCase(argument));
    }

}
