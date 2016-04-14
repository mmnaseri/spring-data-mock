package com.mmnaseri.utils.spring.data.query;

/**
 * This interface represents metadata about a property
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/20/15)
 */
public interface PropertyDescriptor {

    /**
     * @return the path leading to the property
     * @see com.mmnaseri.utils.spring.data.tools.PropertyUtils#getPropertyValue(Object, String)
     */
    String getPath();

    /**
     * @return the type of the property
     */
    Class<?> getType();

}
