package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.lang.reflect.Method;

/**
 * <p>This interface encapsulates the process of resolving the handler for a single unit of work when interacting with an
 * underlying {@link com.mmnaseri.utils.spring.data.store.DataStore data store}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataOperationResolver {

    /**
     * Resolves the data store operation from the given method
     * @param method    the method for which a data operation is required.
     * @return the resolved operation.
     */
    DataStoreOperation<?, ?, ?> resolve(Method method);

}
