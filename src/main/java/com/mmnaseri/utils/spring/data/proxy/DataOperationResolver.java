package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface DataOperationResolver {

    DataStoreOperation<?, ?, ?> resolve(Method method);

}
