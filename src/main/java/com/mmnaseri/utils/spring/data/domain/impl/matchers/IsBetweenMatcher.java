package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * <p>This comparing matcher will determine if the passed value is larger or equal to the first passed argument and
 * smaller or equal to the second passed argument, thus determining if it falls between the two values.</p>
 *
 * <p><strong>NB</strong>: This matcher does not check whether or not the two values passed are in the
 * right order, as a normal database wouldn't. If you need this functionality, you will need to define a new
 * {@link com.mmnaseri.utils.spring.data.domain.Operator operator} and add your own matcher.</p>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsBetweenMatcher extends AbstractBinaryComparableMatcher {

    @Override
    protected boolean matches(Parameter parameter, Comparable value, Comparable first, Comparable second) {
        //noinspection unchecked
        return value != null && first != null && second != null && first.compareTo(value) <= 0 && second.compareTo(value) >= 0;
    }

}
