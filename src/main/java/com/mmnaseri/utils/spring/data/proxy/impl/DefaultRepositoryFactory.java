package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.EventPublishingDataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mmnaseri.utils.spring.data.proxy.dsl.mock.RepositoryMockBuilder.given;
import static com.mmnaseri.utils.spring.data.proxy.dsl.mock.RepositoryMockBuilder.mock;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DefaultRepositoryFactory implements RepositoryFactory {
    
    private final RepositoryMetadataResolver repositoryMetadataResolver;
    private final Map<Class<?>, RepositoryMetadata> metadataMap = new ConcurrentHashMap<Class<?>, RepositoryMetadata>();
    private final QueryDescriptionExtractor descriptionExtractor;
    private final DataFunctionRegistry functionRegistry;
    private final DataStoreRegistry dataStoreRegistry;
    private final ResultAdapterContext adapterContext;
    private final TypeMappingContext typeMappingContext;
    private final RepositoryFactoryConfiguration configuration;

    public DefaultRepositoryFactory(RepositoryFactoryConfiguration configuration) {
        this.configuration = configuration;
        this.repositoryMetadataResolver = configuration.getRepositoryMetadataResolver();
        this.descriptionExtractor = configuration.getDescriptionExtractor();
        this.functionRegistry = configuration.getFunctionRegistry();
        this.dataStoreRegistry = configuration.getDataStoreRegistry();
        this.adapterContext = configuration.getResultAdapterContext();
        this.typeMappingContext = configuration.getTypeMappingContext();
    }

    @Override
    public <E> E getInstance(KeyGenerator<? extends Serializable> keyGenerator, Class<E> repositoryInterface, Class... implementations) {
        final RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
        final DataStore<Serializable, Object> dataStore = getDataStore(metadata);
        final List<TypeMapping<?>> typeMappings = getTypeMappings(metadata, dataStore, keyGenerator, implementations);
        final DataOperationResolver operationResolver = new DefaultDataOperationResolver(typeMappings, descriptionExtractor, metadata, functionRegistry);
        final Method[] methods = repositoryInterface.getMethods();
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = getInvocationMappings(operationResolver, methods);
        //noinspection unchecked
        final InvocationHandler interceptor = new DataOperationInvocationHandler(metadata, invocationMappings, dataStore, adapterContext);
        final Object instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{repositoryInterface}, interceptor);
        for (TypeMapping<?> typeMapping : typeMappings) {
            if (typeMapping.getInstance() instanceof RepositoryAware<?>) {
                //noinspection unchecked
                ((RepositoryAware) typeMapping.getInstance()).setRepository(instance);
            }
        }
        return repositoryInterface.cast(instance);
    }

    @Override
    public RepositoryFactoryConfiguration getConfiguration() {
        return configuration;
    }

    private List<TypeMapping<?>> getTypeMappings(RepositoryMetadata metadata, DataStore<Serializable, Object> dataStore, KeyGenerator<? extends Serializable> keyGenerator, Class[] implementations) {
        final List<TypeMapping<?>> typeMappings = new LinkedList<TypeMapping<?>>();
        final TypeMappingContext localContext = new DefaultTypeMappingContext(typeMappingContext);
        for (Class implementation : implementations) {
            localContext.register(metadata.getRepositoryInterface(), implementation);
        }
        typeMappings.addAll(localContext.getMappings(metadata.getRepositoryInterface()));
        for (TypeMapping<?> mapping : typeMappings) {
            if (mapping.getInstance() instanceof DataStoreAware<?, ?>) {
                DataStoreAware instance = (DataStoreAware<?, ?>) mapping.getInstance();
                instance.setDataStore(dataStore);
            }
            if (mapping.getInstance() instanceof RepositoryMetadataAware) {
                RepositoryMetadataAware instance = (RepositoryMetadataAware) mapping.getInstance();
                instance.setRepositoryMetadata(metadata);
            }
            if (mapping.getInstance() instanceof KeyGeneratorAware) {
                KeyGeneratorAware instance = (KeyGeneratorAware) mapping.getInstance();
                //noinspection unchecked
                instance.setKeyGenerator(keyGenerator);
            }
            if (mapping.getInstance() instanceof RepositoryFactoryConfigurationAware) {
                RepositoryFactoryConfigurationAware instance = (RepositoryFactoryConfigurationAware) mapping.getInstance();
                instance.setRepositoryFactoryConfiguration(configuration);
            }
        }
        return typeMappings;
    }

    private <E> RepositoryMetadata getRepositoryMetadata(Class<E> repositoryInterface) {
        final RepositoryMetadata metadata;
        if (metadataMap.containsKey(repositoryInterface)) {
            metadata = metadataMap.get(repositoryInterface);
        } else {
            metadata = repositoryMetadataResolver.resolve(repositoryInterface);
            metadataMap.put(repositoryInterface, metadata);
        }
        return metadata;
    }

    private DataStore<Serializable, Object> getDataStore(RepositoryMetadata metadata) {
        DataStore<Serializable, Object> dataStore;
        if (dataStoreRegistry.has(metadata.getEntityType())) {
            //noinspection unchecked
            dataStore = (DataStore<Serializable, Object>) dataStoreRegistry.getDataStore(metadata.getEntityType());
        } else {
            //noinspection unchecked
            dataStore = new MemoryDataStore<Serializable, Object>((Class<Object>) metadata.getEntityType());
        }
        if (!(dataStore instanceof EventPublishingDataStore)) {
            dataStore = new EventPublishingDataStore<Serializable, Object>(dataStore, metadata);
        }
        dataStoreRegistry.register(dataStore);
        return dataStore;
    }

    private List<InvocationMapping<? extends Serializable, ?>> getInvocationMappings(DataOperationResolver operationResolver, Method[] methods) {
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = new LinkedList<InvocationMapping<? extends Serializable, ?>>();
        for (Method method : methods) {
            final DataStoreOperation<?, ?, ?> operation = operationResolver.resolve(method);
            //noinspection unchecked
            invocationMappings.add(new ImmutableInvocationMapping<Serializable, Object>(method, (DataStoreOperation<?, Serializable, Object>) operation));
        }
        return invocationMappings;
    }


    public static class Person {

        private String id;
        private String firstName;
        private String lastName;

        public Person() {
        }

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return (id == null ? "(unidentified)" : id) + " - " + lastName + ", " + firstName;
        }
    }

    public interface PersonRepository extends Repository<Person, String> {

        void save(Person sample);

        List<Person> findAll(Sort sort);

        List<Person> findByFirstNameIsNullOrFirstNameIsNotNullOrderByFirstNameAsc();

        int count();

    }

    public static class SampleImpl implements RepositoryFactoryConfigurationAware {

        private StandardPersonRepository personRepository;

        public interface StandardPersonRepository extends CrudRepository<Person, String> {
        }

        @Override
        public void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
            personRepository = given(configuration).generateKeysUsing(UUIDKeyGenerator.class).instantiate(StandardPersonRepository.class);
        }

    }

    public static void main(String[] args) {
        final PersonRepository instance = mock(PersonRepository.class);
        System.out.println(instance.count());
        instance.save(print(new Person("Milad", "Naseri")));
        System.out.println(instance.count());
        instance.save(print(new Person("Maryam", "Naseri")));
        System.out.println(instance.count());
        instance.save(print(new Person("Pouria", "Naseri")));
        System.out.println(instance.count());
        final Iterable<Person> people = instance.findAll(new Sort("firstName"));
        for (Person person : people) {
            System.out.println(person);
            instance.save(person);
        }
        System.out.println(instance.count());
    }

    private static  <E> E print(E e) {
        System.out.println(e);
        return e;
    }

    /*
    final MyRepository repository = givenOperators(operatorContext)
                    .andFunctions(functionRegistry)
                    .withDataStores(dataStoreRegistry)
                    .andAdaptingResultsUsing(resultAdaptorContext)
                    .instantiate(MyRepository.class);
    final RepositoryFactoryConfiguration configuration = givenOperators(operatorContext)
                                                            .andFunctions(functionRegistry)
                                                            .withDataStores(dataStoreRegistry)
                                                            .andAdaptingResultsUsing(resultAdaptorContext)
                                                            .map(Object.class, MyImpl1.class)
                                                            .andMap(Object.class, MyImpl2.class)
                                                            .andMap(Object.class, MyImpl3.class)
                                                            .configure();
    final MyRepository repository = given(configuration).generatingKeysUsing(keyGenerator).andPreferringImplementations(impl1, impl2, impl3).instantiate(MyRepository.class);
    final MyRepository repository = given(configuration).generatingKeysUsing(UUIDKeyGenerator.class).andPreferringImplementations(impl1, impl2, impl3).instantiate(MyRepository.class);
     */


}
