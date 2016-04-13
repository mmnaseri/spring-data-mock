package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface OperatorsAnd extends DataFunctions {

    OperatorsAnd and(Operator operator);

}
