package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 10:25 PM)
 */
public interface FallbackKeyGenerator extends EventListener {

    /**
     * Sets up a default key generator that would be used as a fallback if no key generation scheme is specified for the
     * repository
     * @param keyGenerator    the key generator to be used
     * @return the rest of the configuration
     */
    EventListener withDefaultKeyGenerator(KeyGenerator<?> keyGenerator);

}
