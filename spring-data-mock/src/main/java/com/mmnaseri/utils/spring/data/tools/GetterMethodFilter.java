package com.mmnaseri.utils.spring.data.tools;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * This filter will only accept methods that can be getters for properties (excluding the `is...` format usually used
 * for {@literal boolean} types).
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class GetterMethodFilter implements ReflectionUtils.MethodFilter {

    @Override
    public boolean matches(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        return method.getName().matches("get[A-Z].*") && !void.class.equals(method.getReturnType())
                && method.getParameterTypes().length == 0;
    }

}
