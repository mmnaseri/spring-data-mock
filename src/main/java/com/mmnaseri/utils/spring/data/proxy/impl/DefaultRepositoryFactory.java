package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.EventPublishingDataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mmnaseri.utils.spring.data.proxy.dsl.config.RepositoryFactoryConfigurationBuilder.givenFunctions;
import static com.mmnaseri.utils.spring.data.proxy.dsl.function.FunctionRegistryBuilder.givenFunction;
import static com.mmnaseri.utils.spring.data.proxy.dsl.mock.RepositoryMockBuilder.given;

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
        final DataOperationResolver operationResolver = new DefaultDataOperationResolver(typeMappings, descriptionExtractor, metadata, functionRegistry, configuration);
        final Method[] methods = repositoryInterface.getMethods();
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = getInvocationMappings(operationResolver, methods);
        final List<Class<?>> boundImplementations = new LinkedList<Class<?>>();
        for (TypeMapping<?> mapping : typeMappings) {
            boundImplementations.add(mapping.getType());
        }
        final RepositoryConfiguration repositoryConfiguration = new ImmutableRepositoryConfiguration(metadata, keyGenerator, boundImplementations);
        //noinspection unchecked
        final InvocationHandler interceptor = new DataOperationInvocationHandler(repositoryConfiguration, invocationMappings, dataStore, adapterContext);
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
            dataStore = new EventPublishingDataStore<Serializable, Object>(dataStore, metadata, new DefaultDataStoreEventListenerContext(configuration.getEventListenerContext()));
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

        void duplicate();

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

    private static Object copyObject(Object original) {
        if (original instanceof Serializable) {
            try {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(original);
                outputStream.close();
                final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
                final Object object = inputStream.readObject();
                inputStream.close();
                return object;
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                final Object copy = original.getClass().newInstance();
                final BeanWrapper source = new BeanWrapperImpl(original);
                final BeanWrapper target = new BeanWrapperImpl(copy);
                for (PropertyDescriptor descriptor : source.getPropertyDescriptors()) {
                    if (source.isReadableProperty(descriptor.getName()) && target.isWritableProperty(descriptor.getName())) {
                        target.setPropertyValue(descriptor.getName(), copyObject(source.getPropertyValue(descriptor.getName())));
                    }
                }
                return copy;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static <E> E copy(E object) {
        //noinspection unchecked
        return (E) copyObject(object);
    }

    public static void main(String[] args) {
        final DataFunction<Object> duplicateFunction = new DataFunction<Object>() {
            @Override
            public <K extends Serializable, E> Object apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration configuration, List<E> selection) {
                final List<E> inserted = new LinkedList<E>();
                for (E entity : selection) {
                    final E copy = copy(entity);
                    final BeanWrapper wrapper = new BeanWrapperImpl(copy);
                    final Serializable key = configuration.getKeyGenerator().generate();
                    wrapper.setPropertyValue(query.getRepositoryMetadata().getIdentifier(), key);
                    //noinspection unchecked
                    dataStore.save((K) key, copy);
                    inserted.add(copy);
                }
                return inserted;
            }
        };
        final DataFunctionRegistry registry = givenFunction("duplicate", duplicateFunction).build();
        final RepositoryFactoryConfiguration configuration = givenFunctions(registry).configure();
        final PersonRepository repository = given(configuration).instantiate(PersonRepository.class);
        repository.save(new Person("Milad", "Naseri"));
        repository.duplicate();
        repository.duplicate();
        repository.duplicate();
        repository.duplicate();
        final List<Person> all = repository.findAll(new Sort("firstName"));
        for (Person person : all) {
            System.out.println(person);
        }
        System.out.println(repository.count());
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
