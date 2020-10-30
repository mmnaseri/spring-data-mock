package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class NoIdPropertyException extends EntityDefinitionException {

  public NoIdPropertyException(Class<?> entityType) {
    super("No id property could be resolved for type " + entityType);
  }
}
