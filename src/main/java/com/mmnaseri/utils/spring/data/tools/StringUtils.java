package com.mmnaseri.utils.spring.data.tools;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class StringUtils {

    public static String uncapitalize(String expression) {
        return Objects.requireNonNull(expression, "String value cannot be null").isEmpty() ? expression : expression.substring(0, 1).toLowerCase() + expression.substring(1);
    }

    public static String capitalize(String expression) {
        return Objects.requireNonNull(expression, "String value cannot be null").isEmpty() ? expression : expression.substring(0, 1).toUpperCase() + expression.substring(1);
    }
}
