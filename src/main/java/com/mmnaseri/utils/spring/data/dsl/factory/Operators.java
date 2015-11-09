package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface Operators extends DataFunctions {

    DataFunctions withOperators(OperatorContext context);

    OperatorsAnd registerOperator(Operator operator);

}
