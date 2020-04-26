package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;

/**
 * This interface lets us define the operators that are used when extracting query descriptions
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface Operators extends DataFunctions {

    /**
     * Tells the builder to use the provided context instead of its own context
     *
     * @param context the context
     * @return the rest of the configuration
     */
    DataFunctions withOperators(OperatorContext context);

    /**
     * Registers the given operator in the context used by the builder
     *
     * @param operator the operator
     * @return the rest of the configuration
     */
    OperatorsAnd registerOperator(Operator operator);

}
