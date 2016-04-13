package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface Operators extends DataFunctions {

    DataFunctions withOperators(OperatorContext context);

    OperatorsAnd registerOperator(Operator operator);

}
