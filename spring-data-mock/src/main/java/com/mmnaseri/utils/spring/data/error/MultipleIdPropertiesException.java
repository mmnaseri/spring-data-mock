package com.mmnaseri.utils.spring.data.error;

import java.lang.annotation.Annotation;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class MultipleIdPropertiesException extends EntityDefinitionException {

    public MultipleIdPropertiesException(Class<?> entityType, Class<? extends Annotation> annotationType) {
        super("There are multiple properties in " + entityType + " that are annotated with @" + annotationType.getSimpleName());
    }

}
