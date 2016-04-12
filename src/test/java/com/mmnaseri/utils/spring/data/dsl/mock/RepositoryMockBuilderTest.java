package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import com.mmnaseri.utils.spring.data.error.MockBuilderException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfigurationAware;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.proxy.sample.InformationExposingRepository;
import com.mmnaseri.utils.spring.data.proxy.sample.InformationExposingRepositoryFactory;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.springframework.data.repository.Repository;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/11/16, 2:38 PM)
 */
public class RepositoryMockBuilderTest {

    @Test
    public void testOutOfTheBoxMocking() throws Exception {
        final SampleRepository repository = new RepositoryMockBuilder().mock(SampleRepository.class);
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
        final SampleRepository repository = new RepositoryMockBuilder().withoutGeneratingKeys().mock(SampleRepository.class);
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
        final SampleRepository repository = new RepositoryMockBuilder().generateKeysUsing(CustomStringKeyGenerator.class).mock(SampleRepository.class);
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
        final MappedSampleRepository repository = new RepositoryMockBuilder().usingImplementation(ValueHashMapper.class).and(ValueStringMapper.class).mock(MappedSampleRepository.class);
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
        final SampleRepository repository = new RepositoryMockBuilder().useFactory(new InformationExposingRepositoryFactory(configuration)).mock(SampleRepository.class);
        assertThat(repository, is(instanceOf(InformationExposingRepository.class)));
        final InformationExposingRepository informationExposingRepository = (InformationExposingRepository) repository;
        assertThat(informationExposingRepository.getFactoryConfiguration(), is(notNullValue()));
        assertThat(informationExposingRepository.getFactoryConfiguration(), is(configuration));
        assertThat(informationExposingRepository.getConfiguration(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getBoundImplementations(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getBoundImplementations(), is(empty()));
        assertThat(informationExposingRepository.getConfiguration().getKeyGenerator(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata(), is(notNullValue()));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getEntityType(), is(equalTo(Person.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getIdentifierType(), is(equalTo(String.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getRepositoryInterface(), is(equalTo(SampleRepository.class)));
        assertThat(informationExposingRepository.getConfiguration().getRepositoryMetadata().getIdentifierProperty(), is("id"));
    }

    @Test
    public void testUsingCustomConfiguration() throws Exception {
        final DefaultTypeMappingContext mappingContext = new DefaultTypeMappingContext();
        mappingContext.register(ConfiguredSampleRepository.class, ConfigurationAwareMapper.class);
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        configuration.setDataStoreRegistry(new DefaultDataStoreRegistry());
        configuration.setDescriptionExtractor(new QueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(mappingContext);
        final ConfiguredSampleRepository repository = new RepositoryMockBuilder().useConfiguration(configuration).mock(ConfiguredSampleRepository.class);
        assertThat(repository.getConfiguration(), is(notNullValue()));
        assertThat(repository.getConfiguration(), is(configuration));
    }

    @Test
    public void testNoOpKeyGeneration() throws Exception {
        final RepositoryMockBuilder.NoOpKeyGenerator<Serializable> generator = new RepositoryMockBuilder.NoOpKeyGenerator<>();
        assertThat(generator.generate(), is(nullValue()));
    }

    public static class CustomStringKeyGenerator implements KeyGenerator<String> {

        private final AtomicLong counter = new AtomicLong(0);

        @Override
        public String generate() {
            return String.valueOf(counter.incrementAndGet());
        }

    }

    public interface SampleRepository extends Repository<Person, String> {

        List<Person> findAll();

        void delete(Person person);

        Person save(Person person);

    }

    public interface MappedSampleRepository extends SampleRepository {

        int getHash();

        String getString();

    }

    public interface ConfiguredSampleRepository extends SampleRepository {

        RepositoryFactoryConfiguration getConfiguration();

    }

    public static class ValueHashMapper implements RepositoryAware<MappedSampleRepository> {

        private MappedSampleRepository repository;

        @Override
        public void setRepository(MappedSampleRepository repository) {
            this.repository = repository;
        }

        public int getHash() {
            return repository.hashCode();
        }

    }

    public static class ValueStringMapper implements RepositoryAware<MappedSampleRepository> {

        private MappedSampleRepository repository;

        @Override
        public void setRepository(MappedSampleRepository repository) {
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