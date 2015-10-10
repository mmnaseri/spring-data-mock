package com.mmnaseri.utils.spring.data.proxy.dsl.config;

import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface TypeMappingConfigurer extends BuildFinalizer {

    BuildFinalizer withMappings(TypeMappingContext typeMappingContext);

}
