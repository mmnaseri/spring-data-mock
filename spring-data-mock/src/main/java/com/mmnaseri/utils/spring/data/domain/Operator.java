package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface represents an operator used for matching entities to a preset criteria.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface Operator {

  /** @return the name of the operator. Used for error reporting and such. */
  String getName();

  /**
   * @return the number of operands this operator needs (besides the property on the entity). So,
   *     for instance, an equality check would require <strong>1</strong> operand.
   */
  int getOperands();

  /**
   * @return the matcher taking care of checking whether or not the operator applies to the current
   *     entity
   */
  Matcher getMatcher();

  /** @return the suffix tokens used to indicate this operator in the name of a query method */
  String[] getTokens();
}
