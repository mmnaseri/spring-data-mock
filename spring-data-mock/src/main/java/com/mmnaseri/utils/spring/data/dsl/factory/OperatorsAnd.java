package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;

/**
 * This is a conjunction that let's us define an additional operator to register
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface OperatorsAnd extends DataFunctions {

    /**
     * Registers this operator as well
     *
     * @param operator the operator
     * @return the rest of the configuration
     */
    OperatorsAnd and(Operator operator);

}
