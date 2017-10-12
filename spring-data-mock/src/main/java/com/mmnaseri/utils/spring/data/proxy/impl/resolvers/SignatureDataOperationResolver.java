package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
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
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        log.info("Trying to resolve the data operation for method " + method + " by going through the previously set up type mappings");
        for (TypeMapping<?> mapping : mappings) {
            final Class<?> type = mapping.getType();
            final Method declaration = findMethod(type, method.getName(), method.getParameterTypes());
            if (declaration != null) {
                log.info("Setting the resolution as a method invocation on the previously prepared type mapping");
                final Object instance = mapping.getInstance();
                return new MethodInvocationDataStoreOperation<Serializable, Object>(instance, declaration);
            }
        }
        return null;
    }

    private Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        log.debug("Attempting to look for the actual declaration of the method named '" + name + "' with parameter types " + Arrays.toString(parameterTypes) + " on the child type " + type);
        Class<?> searchType = type;

        List<Method> matchingMethods = new LinkedList<>();

        while (searchType != null) {
            log.trace("Looking at type " + type + " for method " + name);
            final Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(name) && parameterTypes.length == method.getParameterTypes().length) {
                    boolean matches = true;
                    for (int i = 0; i < parameterTypes.length; i++) {
                        final Class<?> parameterType = parameterTypes[i];
                        if (!doParamsMatchByAssignability(method.getParameterTypes()[i], PropertyUtils.getTypeOf(parameterType))) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        matchingMethods.add(method);
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }

        if (matchingMethods.size() == 0) {
            return null;
        } else if (matchingMethods.size() == 1) {
            return matchingMethods.get(0);
        } else {
            Method method = exactlyMatchingMethod(matchingMethods, parameterTypes);
            if (method == null) {
                return matchingMethods.get(matchingMethods.size() - 1);
            } else {
                return method;
            }
        }
    }

    private Method exactlyMatchingMethod(List<Method> methods, Class<?>[] parameterTypes) {
        for (Method method : methods) {
            boolean matches = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                final Class<?> parameterType = parameterTypes[i];
                if (!(doParamsMatchExactly(method.getParameterTypes()[i], PropertyUtils.getTypeOf(parameterType)))) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return method;
            }
        }
        return null;
    }

    protected boolean doParamsMatchByAssignability(Class<?> thisParam, Class<?> thatParam) {
        return PropertyUtils.getTypeOf(thisParam).isAssignableFrom(thatParam);
    }


    protected boolean doParamsMatchExactly(Class<?> thisParam, Class<?> thatParam) {
        return thisParam.getCanonicalName().equals(thatParam.getCanonicalName());
    }
}
