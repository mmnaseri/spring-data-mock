package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface InvocationMapping<K extends Serializable, E> {

    Method getMethod();

    DataStoreOperation<?, K, E> getOperation();

}
