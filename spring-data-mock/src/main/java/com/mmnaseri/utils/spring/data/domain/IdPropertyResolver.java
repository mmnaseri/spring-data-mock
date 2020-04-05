package com.mmnaseri.utils.spring.data.domain;

/**
 * An id property resolver will be capable of looking at an entity class and find the name of the property that
 * is the ID property of that class based on the expected type of the identifier.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
public interface IdPropertyResolver {

    /**
     * Resolves the name of the ID <em>property</em>. If the property is accessible through a getter, it will
     * still return the name of the underlying property accessible by the getter by converting the name of the getter
     * to the bare property name.
     * @param entityType    the type of the entity on which the key is defined
     * @param idType        the expected type (or supertype) for the ID property
     * @return the name of the property that represents the key to the entity
     */
    String resolve(Class<?> entityType, Class<?> idType);

}
