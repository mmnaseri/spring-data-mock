package com.mmnaseri.utils.samples.spring.data.jpa.utils;

import com.mmnaseri.utils.samples.spring.data.jpa.repository.SerialAwareEntityRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.SerialAwareEntityRepositorySupport;
import com.mmnaseri.utils.spring.data.domain.impl.key.ConfigurableSequentialLongKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;

/**
 * We are setting defaults on the configuration level, which we will be reusing later.
 *
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:42 PM)
 */
public final class RepositoryConfigUtils {

    private static final RepositoryFactoryConfiguration CONFIGURATION = builder()
            .honoringImplementation(SerialAwareEntityRepository.class, SerialAwareEntityRepositorySupport.class)
            .withDefaultKeyGenerator(new ConfigurableSequentialLongKeyGenerator())
            .configure();

    private RepositoryConfigUtils() {
        throw new UnsupportedOperationException();
    }

    public static RepositoryFactoryConfiguration getConfiguration() {
        return CONFIGURATION;
    }

}
