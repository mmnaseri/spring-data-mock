/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 3:09)
 */
public abstract class AbstractTypedMatcher implements Matcher {

    private final Class<?> superType;
    private final int parameters;

    public AbstractTypedMatcher(Class<?> superType, int parameters) {
        this.superType = superType;
        this.parameters = parameters;
    }

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        if (!superType.isInstance(value)) {
            throw new IllegalArgumentException("Parameter not of the expected type: " + superType);
        }
        for (int i = 0; i < this.parameters; i ++) {
            Objects.requireNonNull(parameters[i]);
            if (!superType.isInstance(parameters[i])) {
                throw new IllegalArgumentException("Parameter not of the expected type: " + superType);
            }
        }
        return doMatch(parameter, value, parameters);
    }

    protected abstract boolean doMatch(Parameter parameter, Object value, Object[] parameters);
}
