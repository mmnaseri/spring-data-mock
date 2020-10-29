package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.Parameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is an immutable parameter.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class ImmutableParameter implements Parameter {

  private final String path;
  private final Set<Modifier> modifiers;
  private final int[] indices;
  private final Operator operator;

  public ImmutableParameter(
      String path, Set<Modifier> modifiers, int[] indices, Operator operator) {
    this.path = path;
    this.operator = operator;
    this.modifiers = modifiers == null ? Collections.emptySet() : new HashSet<>(modifiers);
    this.indices = indices;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return Collections.unmodifiableSet(modifiers);
  }

  @Override
  public int[] getIndices() {
    return indices;
  }

  @Override
  public Operator getOperator() {
    return operator;
  }

  @Override
  public String toString() {
    return "(" + path + "," + operator + "," + Arrays.toString(indices) + "," + modifiers + ")";
  }
}
