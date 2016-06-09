package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class MultipleIdPropertiesException extends EntityDefinitionException {

    public MultipleIdPropertiesException(Class<?> entityType) {
        super("There are multiple properties in " + entityType + " that are annotated as the ID property");
    }

}
