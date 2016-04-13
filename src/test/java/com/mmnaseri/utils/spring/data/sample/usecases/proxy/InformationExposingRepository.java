package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 11:12 AM)
 */
public interface InformationExposingRepository {

    RepositoryFactoryConfiguration getFactoryConfiguration();

    RepositoryConfiguration getConfiguration();

}
