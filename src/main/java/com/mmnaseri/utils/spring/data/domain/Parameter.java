package com.mmnaseri.utils.spring.data.domain;

import java.util.Set;

/**
 * This interface represents a "parameter" factored into matching a given entity to a preset criteria.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface Parameter {

    /**
     * @return the path leading to the property. This could be a nested property using the "dot notation" to separate
     */
    String getPath();

    /**
     * @return the modifiers applying to the parameter
     */
    Set<Modifier> getModifiers();

    /**
     * @return actual indices from the query method that map to the operands for this parameter. It should always follow
     * that {@literal getIndices().length == getOperator().getOperands()}.
     */
    int[] getIndices();

    /**
     * @return the operator for this parameter
     */
    Operator getOperator();

}
