package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.sample.mocks.CustomStringKeyGenerator;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import com.mmnaseri.utils.spring.data.error.MockBuilderException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfigurationAware;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.sample.repositories.SimpleCrudPersonRepository;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.InformationExposingRepository;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.InformationExposingRepositoryFactory;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.hamcrest.Matchers;
import org.springframework.data.repository.Repository;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/11/16, 2:38 PM)
 */
public class RepositoryMockBuilderTest {

    @Test
    public void testOutOfTheBoxMocking() throws Exception {
        final SimpleCrudPersonRepository repository = new RepositoryMockBuilder().mock(SimpleCrudPersonRepository.class);
        assertThat(repository, is(notNullValue()));
        final Person person = repository.save(new Person());
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
        assertThat(repository.findAll(), hasItem(person));
        repository.delete(person);
        assertThat(repository.findAll(), is(empty()));
    }

    @Test
    public void testMockingWithoutKeyGeneration() throws Exception {
        final SimpleCrudPersonRepository repository = new RepositoryMockBuilder().withoutGeneratingKeys().mock(SimpleCrudPersonRepository.class);
        assertThat(repository, is(notNullValue()));
        boolean exceptionThrown = false;
        try {
            repository.save(new Person());
        } catch (Exception e) {
            assertThat(e, is(instanceOf(DataOperationExecutionException.class)));
            assertThat(e.getCause(), is(notNullValue()));
            assertThat(e.getCause(), is(instanceOf(CorruptDataException.class)));
            exceptionThrown = true;
        }
        assertThat(exceptionThrown, is(true));
        final Person person = repository.save(new Person().setId("1"));
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
        assertThat(repository.findAll(), hasItem(person));
        repository.delete(person);
        assertThat(repository.findAll(), is(empty()));
    }

    @Test
    public void testCustomKeyGeneration() throws Exception {
        final SimpleCrudPersonRepository repository = new RepositoryMockBuilder().generateKeysUsing(CustomStringKeyGenerator.class).mock(SimpleCrudPersonRepository.class);
        assertThat(repository, is(notNullValue()));
        final Person person = repository.save(new Person());
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
        assertThat(repository.findAll(), hasItem(person));
        repository.delete(person);
        assertThat(repository.findAll(), is(empty()));
    }

    @Test(expectedExceptions = MockBuilderException.class)
    public void testUsingInvalidKeyGenerator() throws Exception {
        new RepositoryMockBuilder().generateKeysUsing(InaccessibleKeyGenerator.class);
    }

    @Test
    public void testUsingCustomImplementations() throws Exception {
        final MappedSimpleCrudPersonRepository repository = new RepositoryMockBuilder().usingImplementation(ValueHashMapper.class).and(ValueStringMapper.class).mock(MappedSimpleCrudPersonRepository.class);
        assertThat(repository, is(notNullValue()));
        final Person person = repository.save(new Person());
        assertThat(repository.findAll(), is(notNullValue()));
        assertThat(repository.findAll(), hasSize(1));
        assertThat(repository.findAll(), hasItem(person));
        repository.delete(person);
        assertThat(repository.findAll(), is(empty()));
        assertThat(repository.getHash(), is(repository.hashCode()));
        assertThat(repository.getString(), is(repository.toString()));
    }

    /**
     * This is a slightly more sophisticated test that adds additional methods to the interface being mocked.
     * <p>
     * This sort of thing might come in handy if we are unsure of how to proceed with the tests, or if there is something we need bound to the repository
     * specifically for the tests, however, it should be noted that adding functionality to your repositories for the purpose of testing
     * is not really the greatest idea.
     *
     * @throws Exception
     */
    @Test
    public void testUsingCustomFactory() throws Exception {
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        configuration.setDataStoreRegistry(new DefaultDataStoreRegistry());
        configuration.setDescriptionExtractor(new QueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(new DefaultTypeMappingContext());
        final SimpleCrudPersonRepository repository = new RepositoryMockBuilder().useFactory(new InformationExposingRepositoryFactory(configuration)).mock(SimpleCrudPersonRepository.class);
        assertThat(repository, is(instanceOf(InformationExposingRepository.class)));
        final InformationExposingRepository informationExposingRepository = (InformationExposingRepository) repository;
        assertThat(informationExposingRepository.getFactoryConfiguration(), is(notNullValue()));
        assertThat(informationExposingRepository.getFactoryConfiguration(), Matchers.<RepositoryFactoryConfiguration>is(configuration));
        assertThat(informationExposingRepository.getConfiguration(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getBoundImplementations(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getBoundImplementations(), is(empty()));
        assertThat(informationExposingRepository.getConfiguration().getKeyGenerator(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getEntityType(), is(Matchers.<Class<?>>equalTo(Person.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getIdentifierType(), is(Matchers.<Class<?>>equalTo(String.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getRepositoryInterface(), is(Matchers.<Class<?>>equalTo(SimpleCrudPersonRepository.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getIdentifierProperty(), is("id"));
    }

    @Test
    public void testUsingCustomConfiguration() throws Exception {
        final DefaultTypeMappingContext mappingContext = new DefaultTypeMappingContext();
        mappingContext.register(ConfiguredSimpleCrudPersonRepository.class, ConfigurationAwareMapper.class);
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        configuration.setDataStoreRegistry(new DefaultDataStoreRegistry());
        configuration.setDescriptionExtractor(new QueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(mappingContext);
        final ConfiguredSimpleCrudPersonRepository repository = new RepositoryMockBuilder().useConfiguration(configuration).mock(ConfiguredSimpleCrudPersonRepository.class);
        assertThat(repository.getConfiguration(), is(notNullValue()));
        assertThat(repository.getConfiguration(), Matchers.<RepositoryFactoryConfiguration>is(configuration));
    }

    @Test
    public void testNoOpKeyGeneration() throws Exception {
        final RepositoryMockBuilder.NoOpKeyGenerator<Serializable> generator = new RepositoryMockBuilder.NoOpKeyGenerator<>();
        assertThat(generator.generate(), is(nullValue()));
    }

    public interface MappedSimpleCrudPersonRepository extends SimpleCrudPersonRepository {

        int getHash();

        String getString();

    }

    public interface ConfiguredSimpleCrudPersonRepository extends SimpleCrudPersonRepository {

        RepositoryFactoryConfiguration getConfiguration();

    }

    public static class ValueHashMapper implements RepositoryAware<MappedSimpleCrudPersonRepository> {

        private MappedSimpleCrudPersonRepository repository;

        @Override
        public void setRepository(MappedSimpleCrudPersonRepository repository) {
            this.repository = repository;
        }

        public int getHash() {
            return repository.hashCode();
        }

    }

    public static class ValueStringMapper implements RepositoryAware<MappedSimpleCrudPersonRepository> {

        private MappedSimpleCrudPersonRepository repository;

        @Override
        public void setRepository(MappedSimpleCrudPersonRepository repository) {
            this.repository = repository;
        }

        public String getString() {
            return repository.toString();
        }

    }

    public static class ConfigurationAwareMapper implements RepositoryFactoryConfigurationAware {


        private RepositoryFactoryConfiguration configuration;

        @Override
        public void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
            this.configuration = configuration;
        }

        public RepositoryFactoryConfiguration getConfiguration() {
            return configuration;
        }

    }

    public static class InaccessibleKeyGenerator implements KeyGenerator<Serializable> {

        private InaccessibleKeyGenerator() {
        }

        @Override
        public Serializable generate() {
            return null;
        }

    }

}