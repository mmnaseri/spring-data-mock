package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This class will look for a substring in the passed string value by converting both the needle and
 * the haystack to lower case.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class ContainingMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return actual != null && argument != null && actual.toLowerCase().contains(argument.toLowerCase());
    }

}
