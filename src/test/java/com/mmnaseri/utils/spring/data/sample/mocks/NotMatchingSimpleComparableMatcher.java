package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractSimpleComparableMatcher;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:00 PM)
 */
public class NotMatchingSimpleComparableMatcher extends AbstractSimpleComparableMatcher {

    @Override
    protected boolean matches(Parameter parameter, Comparable actual, Comparable pivot) {
        return false;
    }

}
