package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * <p>This class will resolve methods to the equivalent methods of the mapped implementations.</p>
 *
 * <p>The process of equating methods to their implementation finds methods on the nearest implementation
 * that can handle the given parameters, and whose name is the exact same as the invoked method.</p>
 *
 * <p>Since the return value of the method can and will be adapted to the return value of the invoked method,
 * return values are not considered to be so important, and they are not checked or considered.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class SignatureDataOperationResolver implements DataOperationResolver {

    private static final Log log = LogFactory.getLog(SignatureDataOperationResolver.class);
    private final List<TypeMapping<?>> mappings;

    public SignatureDataOperationResolver(List<TypeMapping<?>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public DataStoreOperation<?, ?, ?> resolve(Method method) {
        log.info("Trying to resolve the data operation for method " + method
                         + " by going through the previously set up type mappings");
        for (TypeMapping<?> mapping : mappings) {
            final Class<?> type = mapping.getType();
            final Method declaration = findMethod(type, method.getName(), method.getParameterTypes());
            if (declaration != null) {
                log.info("Setting the resolution as a method invocation on the previously prepared type mapping");
                final Object instance = mapping.getInstance();
                return new MethodInvocationDataStoreOperation<>(instance, declaration);
            }
        }
        return null;
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        log.debug(
                "Attempting to look for the actual declaration of the method named '" + name + "' with parameter types "
                        + Arrays.toString(parameterTypes) + " on the child type " + type);
        Class<?> searchType = type;
        while (searchType != null) {
            log.trace("Looking at type " + type + " for method " + name);
            final Method[] methods =
                    searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(name) && parameterTypes.length == method.getParameterTypes().length) {
                    boolean matches = true;
                    for (int i = 0; i < parameterTypes.length; i++) {
                        final Class<?> parameterType = parameterTypes[i];
                        if (!PropertyUtils.getTypeOf(method.getParameterTypes()[i]).isAssignableFrom(
                                PropertyUtils.getTypeOf(parameterType))) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        return method;
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

}
