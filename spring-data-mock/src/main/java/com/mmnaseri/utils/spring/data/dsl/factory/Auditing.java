package com.mmnaseri.utils.spring.data.dsl.factory;

import org.springframework.data.domain.AuditorAware;

/**
 * Lets us set up out-of-the-box auditing
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/13/16, 11:20 AM)
 */
@SuppressWarnings("WeakerAccess")
public interface Auditing extends End {

  /**
   * Enables auditing by using the provided auditor aware
   *
   * @param auditorAware the auditor aware providing auditor
   * @return the rest of the configuration
   */
  End enableAuditing(AuditorAware auditorAware);

  /**
   * Enables auditing by setting the auditor to {@link
   * com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.DefaultAuditorAware the
   * default} value.
   *
   * @return the rest of the configuration
   */
  End enableAuditing();
}
