package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.domain.impl.SelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfigurationAware;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfigurationAware;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.data.domain.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/8/16, 11:57 AM)
 */
public class DefaultQueryByExampleExecutor implements DataStoreAware, RepositoryConfigurationAware, RepositoryMetadataAware, RepositoryFactoryConfigurationAware {

    private DataStore<Serializable, Object> dataStore;
    private final ExampleMatcherQueryDescriptionExtractor queryDescriptionExtractor;
    private RepositoryConfiguration repositoryConfiguration;
    private RepositoryMetadata repositoryMetadata;
    private RepositoryFactoryConfiguration configuration;

    public DefaultQueryByExampleExecutor() {
        queryDescriptionExtractor = new ExampleMatcherQueryDescriptionExtractor();
    }

    public Object findOne(Example example) {
        final Collection found = retrieveAll(example);
        if (found.isEmpty()) {
            return null;
        } else if (found.size() > 1) {
            throw new InvalidArgumentException("Expected to see exactly one item found, but found " + found.size() + ". You should use a better example.");
        }
        return found.iterator().next();
    }

    public Iterable findAll(Example example) {
        return retrieveAll(example);
    }

    public Iterable findAll(Example example, Sort sort) {
        return PagingAndSortingUtils.sort(retrieveAll(example), sort);
    }

    public Page findAll(Example example, Pageable pageable) {
        return PagingAndSortingUtils.page(retrieveAll(example), pageable);
    }

    public long count(Example example) {
        return (long) retrieveAll(example).size();
    }

    public boolean exists(Example example) {
        return count(example) > 0;
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        //noinspection unchecked
        this.dataStore = dataStore;
    }

    @Override
    public void setRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) {
        this.repositoryConfiguration = repositoryConfiguration;
    }

    @Override
    public void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
    }

    @Override
    public void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Retrieves all entities that match the given example
     * @param example    the example for finding the entities
     * @return a collection of matched entities
     */
    private Collection<?> retrieveAll(Example example) {
        final QueryDescriptor descriptor = queryDescriptionExtractor.extract(repositoryMetadata, configuration, example);
        final Invocation invocation = createInvocation(descriptor, example);
        final SelectDataStoreOperation<Serializable, Object> select = new SelectDataStoreOperation<>(descriptor);
        return select.execute(dataStore, repositoryConfiguration, invocation);
    }

    /**
     * This method will create an invocation that had it occurred on a query method would provide sufficient
     * data for a parsed query method expression to be evaluated
     * @param descriptor    the query descriptor
     * @param example       the example that is used for evaluation
     * @return the fake method invocation corresponding to the example probe
     */
    private Invocation createInvocation(QueryDescriptor descriptor, Example example) {
        final List<Object> values = new ArrayList<>();
        //since according to http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example
        //the matcher only supports AND condition, so, we expect to see only one branch
        final List<List<Parameter>> branches = descriptor.getBranches();
        final List<Parameter> parameters = branches.get(0);
        for (Parameter parameter : parameters) {
            final String propertyPath = parameter.getPath();
            final Object propertyValue = PropertyUtils.getPropertyValue(example.getProbe(), propertyPath);
            final ExampleMatcher.PropertySpecifier specifier = example.getMatcher().getPropertySpecifiers().getForPath(propertyPath);
            values.add(specifier == null ? propertyValue : specifier.getPropertyValueTransformer().convert(propertyValue));
        }
        return new ImmutableInvocation(null, values.toArray());
    }

}
