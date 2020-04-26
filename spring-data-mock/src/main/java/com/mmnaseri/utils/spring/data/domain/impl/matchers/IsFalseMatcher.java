package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This will check to see if the value on the entity object is {@literal false}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsFalseMatcher extends AbstractStateMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value) {
        return value != null && Boolean.FALSE.equals(value);
    }

}
