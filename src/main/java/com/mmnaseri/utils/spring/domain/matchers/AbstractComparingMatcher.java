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

package com.mmnaseri.utils.spring.domain.matchers;

import com.mmnaseri.utils.spring.domain.Matcher;
import com.mmnaseri.utils.spring.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:51)
 */
public abstract class AbstractComparingMatcher implements Matcher {

    public abstract int compareFirstToSecond();

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        Objects.requireNonNull(parameters[0]);
        if (parameters[0] instanceof Comparable && value instanceof Comparable) {
            Comparable comparable = (Comparable) parameters[0];
            if (parameter.isIgnoreCase()) {
                if (value instanceof CharSequence) {
                    value = value.toString().toLowerCase();
                }
                if (comparable instanceof CharSequence) {
                    comparable = comparable.toString().toLowerCase();
                }
                parameters[0] = parameters[0].toString().toLowerCase();
            }
            if (value.getClass().isAssignableFrom(comparable.getClass())) {
                //noinspection unchecked
                final int comparison = comparable.compareTo(value);
                final int compareFirstToSecond = compareFirstToSecond();
                if (compareFirstToSecond == 0) {
                    return comparison == 0;
                } else {
                    return comparison * compareFirstToSecond() > 0;
                }
            } else {
                throw new IllegalArgumentException("Parameter types do not match");
            }
        }
        throw new IllegalArgumentException("Parameters are not comparable");
    }

}
