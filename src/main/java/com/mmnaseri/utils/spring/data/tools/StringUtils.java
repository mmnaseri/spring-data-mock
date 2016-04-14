package com.mmnaseri.utils.spring.data.tools;

import java.util.Objects;

/**
 * Utility class for working with String objects.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the same string as the original value, with the first letter turned to lower case
     * @param expression    the expression
     * @return the un-capitalized version of the expression
     */
    public static String uncapitalize(String expression) {
        return Objects.requireNonNull(expression, "String value cannot be null").isEmpty() ? expression : expression.substring(0, 1).toLowerCase() + expression.substring(1);
    }

    /**
     * Returns the same string as the original value, with the first letter turned to upper case
     * @param expression    the expression
     * @return the un-capitalized version of the expression
     */
    public static String capitalize(String expression) {
        return Objects.requireNonNull(expression, "String value cannot be null").isEmpty() ? expression : expression.substring(0, 1).toUpperCase() + expression.substring(1);
    }

}
