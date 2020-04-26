package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This will check to see if the value on the entity object is {@literal true}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsTrueMatcher extends AbstractStateMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value) {
        return value != null && Boolean.TRUE.equals(value);
    }

}
