package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

/**
 * This class is an immutable repository factory configuration insofar as the configuration itself
 * is considered. The properties themselves won't be protected and are open to external change.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 1:49 PM)
 */
@SuppressWarnings("WeakerAccess")
public class ImmutableRepositoryFactoryConfiguration implements RepositoryFactoryConfiguration {

  private final RepositoryMetadataResolver metadataResolver;
  private final MethodQueryDescriptionExtractor queryDescriptionExtractor;
  private final DataFunctionRegistry functionRegistry;
  private final DataStoreRegistry dataStoreRegistry;
  private final ResultAdapterContext resultAdapterContext;
  private final TypeMappingContext typeMappingContext;
  private final DataStoreEventListenerContext eventListenerContext;
  private final NonDataOperationInvocationHandler operationInvocationHandler;
  private final KeyGenerator<?> keyGenerator;

  public ImmutableRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
    this(
        configuration.getRepositoryMetadataResolver(),
        configuration.getDescriptionExtractor(),
        configuration.getFunctionRegistry(),
        configuration.getDataStoreRegistry(),
        configuration.getResultAdapterContext(),
        configuration.getTypeMappingContext(),
        configuration.getEventListenerContext(),
        configuration.getOperationInvocationHandler(),
        configuration.getDefaultKeyGenerator());
  }

  public ImmutableRepositoryFactoryConfiguration(
      RepositoryMetadataResolver metadataResolver,
      MethodQueryDescriptionExtractor queryDescriptionExtractor,
      DataFunctionRegistry functionRegistry,
      DataStoreRegistry dataStoreRegistry,
      ResultAdapterContext resultAdapterContext,
      TypeMappingContext typeMappingContext,
      DataStoreEventListenerContext eventListenerContext,
      NonDataOperationInvocationHandler operationInvocationHandler,
      KeyGenerator<?> keyGenerator) {
    this.metadataResolver = metadataResolver;
    this.queryDescriptionExtractor = queryDescriptionExtractor;
    this.functionRegistry = functionRegistry;
    this.dataStoreRegistry = dataStoreRegistry;
    this.resultAdapterContext = resultAdapterContext;
    this.typeMappingContext = typeMappingContext;
    this.eventListenerContext = eventListenerContext;
    this.operationInvocationHandler = operationInvocationHandler;
    this.keyGenerator = keyGenerator;
  }

  @Override
  public RepositoryMetadataResolver getRepositoryMetadataResolver() {
    return metadataResolver;
  }

  @Override
  public MethodQueryDescriptionExtractor getDescriptionExtractor() {
    return queryDescriptionExtractor;
  }

  @Override
  public DataFunctionRegistry getFunctionRegistry() {
    return functionRegistry;
  }

  @Override
  public DataStoreRegistry getDataStoreRegistry() {
    return dataStoreRegistry;
  }

  @Override
  public ResultAdapterContext getResultAdapterContext() {
    return resultAdapterContext;
  }

  @Override
  public TypeMappingContext getTypeMappingContext() {
    return typeMappingContext;
  }

  @Override
  public DataStoreEventListenerContext getEventListenerContext() {
    return eventListenerContext;
  }

  @Override
  public NonDataOperationInvocationHandler getOperationInvocationHandler() {
    return operationInvocationHandler;
  }

  @Override
  public KeyGenerator<?> getDefaultKeyGenerator() {
    return keyGenerator;
  }
}
