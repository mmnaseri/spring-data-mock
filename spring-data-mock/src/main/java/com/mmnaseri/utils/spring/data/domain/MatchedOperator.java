package com.mmnaseri.utils.spring.data.domain;

/**
 * Represents an operator that was picked because of a parse operation
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/17/16, 12:36 PM)
 */
public interface MatchedOperator extends Operator {

    /**
     * @return the suffix that was matched when looking up this operator
     */
    String getMatchedToken();

}
