package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public abstract class AbstractIdPropertyResolver implements IdPropertyResolver {

    protected static String uncapitalize(String expression) {
        return expression.substring(0, 1).toLowerCase() + expression.substring(1);
    }

}
