package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This class is used to find out if the given value starts with the passed argument. Remember that this
 * will convert both the needle and the haystack to lower case, so the search is case-insensitive.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class StartingWithMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return actual != null && argument != null && actual.toLowerCase().startsWith(argument.toLowerCase());
    }

}
