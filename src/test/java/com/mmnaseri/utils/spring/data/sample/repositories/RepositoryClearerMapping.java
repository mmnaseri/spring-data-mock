package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryTest;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:48 PM)
 */
public class RepositoryClearerMapping implements RepositoryAware<ClearableSimpleCrudPersonRepository>, RepositoryFactoryConfigurationAware, RepositoryConfigurationAware, RepositoryFactoryAware {

    private ClearableSimpleCrudPersonRepository repository;

    @Override
    public void setRepository(ClearableSimpleCrudPersonRepository repository) {
        this.repository = repository;
    }

    public void clearRepo() {
        repository.deleteAll();
    }

    @Override
    public void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
        assertThat(configuration, is(notNullValue()));
    }

    @Override
    public void setRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) {
        assertThat(repositoryConfiguration, is(notNullValue()));
        assertThat(repositoryConfiguration.getKeyGenerator(), is(notNullValue()));
        assertThat(repositoryConfiguration.getKeyGenerator(), is(instanceOf(UUIDKeyGenerator.class)));
        assertThat(repositoryConfiguration.getRepositoryMetadata(), is(notNullValue()));
        assertThat(repositoryConfiguration.getRepositoryMetadata().getEntityType(), is(Matchers.<Class<?>>equalTo(Person.class)));
        assertThat(repositoryConfiguration.getRepositoryMetadata().getIdentifierProperty(), is("id"));
        assertThat(repositoryConfiguration.getRepositoryMetadata().getIdentifierType(), is(Matchers.<Class<?>>equalTo(String.class)));
        assertThat(repositoryConfiguration.getRepositoryMetadata().getRepositoryInterface(), is(Matchers.<Class<?>>equalTo(ClearableSimpleCrudPersonRepository.class)));
        assertThat(repositoryConfiguration.getBoundImplementations(), is(not(empty())));
        assertThat(repositoryConfiguration.getBoundImplementations(), hasItem(RepositoryClearerMapping.class));
    }

    @Override
    public void setRepositoryFactory(RepositoryFactory factory) {
        assertThat(factory, is(notNullValue()));
    }
}
