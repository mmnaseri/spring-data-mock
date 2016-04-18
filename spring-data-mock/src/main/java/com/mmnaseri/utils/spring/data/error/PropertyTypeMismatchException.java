package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class PropertyTypeMismatchException extends EntityDefinitionException {

    public PropertyTypeMismatchException(Class<?> declaringClass, String propertyName, Class<?> expectedType, Class<?> actualType) {
        super("Expected property <" + propertyName + "> of class <" + declaringClass + "> to be of type <" + expectedType + "> but it was of type <" + actualType + ">");
    }

}
