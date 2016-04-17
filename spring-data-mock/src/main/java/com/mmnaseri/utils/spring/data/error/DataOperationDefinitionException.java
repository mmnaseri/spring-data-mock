package com.mmnaseri.utils.spring.data.error;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class DataOperationDefinitionException extends DataOperationException {

    public DataOperationDefinitionException(Method method, Throwable cause) {
        super("Encountered an error while resolving operation metadata: " + method, cause);
    }

}
