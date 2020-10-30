/**
 * This package implements the bridge pattern over the various repository interfaces in the Spring
 * Data project. Each class should represent exactly one repository interface in the Spring data
 * project.
 *
 * <p>Many of the interfaces in the Spring data project come with their own packaging and project.
 * We have to check for the presence of such an interface before adding the bridge for it to the
 * classpath.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 8:47 PM)
 */
package com.mmnaseri.utils.spring.data.repository;
