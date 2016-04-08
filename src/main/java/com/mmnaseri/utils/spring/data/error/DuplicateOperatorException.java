package com.mmnaseri.utils.spring.data.error;

import com.mmnaseri.utils.spring.data.domain.Operator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class DuplicateOperatorException extends OperatorContextException {
    public DuplicateOperatorException(Operator existing, String token) {
        super("Another operator (" + existing.getName() + ") already defines answers to this token: " + token);
    }
}
