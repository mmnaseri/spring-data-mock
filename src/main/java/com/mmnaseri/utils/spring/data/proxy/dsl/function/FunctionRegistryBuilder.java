package com.mmnaseri.utils.spring.data.proxy.dsl.function;

import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class FunctionRegistryBuilder implements BuildInitializer, FunctionRegistrationConjunction {
    
    private final DataFunctionRegistry registry;

    public static FunctionRegistration givenRegistry(DataFunctionRegistry registry) {
        return new FunctionRegistryBuilder().startingWith(registry);
    }

    public static <R> FunctionRegistrationConjunction givenFunction(String name, DataFunction<R> function) {
        return new FunctionRegistryBuilder().and(name, function);
    }

    private FunctionRegistryBuilder() {
        this(new DefaultDataFunctionRegistry());
    }

    private FunctionRegistryBuilder(DataFunctionRegistry registry) {
        this.registry = registry;
    }

    private DataFunctionRegistry copy(DataFunctionRegistry registry) {
        final DataFunctionRegistry starting = new DefaultDataFunctionRegistry();
        for (String functionName : registry.getFunctions()) {
            starting.register(functionName, registry.getFunction(functionName));
        }
        return starting;
    }

    @Override
    public FunctionRegistration startingWith(DataFunctionRegistry registry) {
        return new FunctionRegistryBuilder(copy(registry));
    }

    @Override
    public <R> FunctionRegistrationConjunction register(String name, DataFunction<R> function) {
        final DataFunctionRegistry registry = copy(this.registry);
        registry.register(name, function);
        return new FunctionRegistryBuilder(registry);
    }

    @Override
    public <R> FunctionRegistrationConjunction and(String name, DataFunction<R> function) {
        return register(name, function);
    }

    @Override
    public DataFunctionRegistry build() {
        return copy(registry);
    }
    
}
