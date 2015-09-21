package com.mmnaseri.utils.spring.data.tools;

import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;
import com.mmnaseri.utils.spring.data.query.impl.ImmutablePropertyDescriptor;
import com.mmnaseri.utils.spring.data.string.DocumentReader;
import com.mmnaseri.utils.spring.data.string.impl.DefaultDocumentReader;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public abstract class PropertyUtils {

    public static Object getPropertyValue(Object context, String property) {
        BeanWrapper wrapper = new BeanWrapperImpl(context);
        while (property.contains(".")) {
            final String[] split = property.split("\\.", 2);
            wrapper = new BeanWrapperImpl(wrapper.getPropertyValue(split[0]));
            property = split[1];
        }
        return wrapper.getPropertyValue(property);
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> domainType, String expression) {
        final DocumentReader reader = new DefaultDocumentReader(expression);
        String path = "";
        String property = "";
        Class<?> context = domainType;
        while (reader.hasMore()) {
            boolean explicit = false;
            final String token = reader.rest().contains("_") ? reader.expect("[^_]+") : reader.expect("[A-Z][a-z]*");
            if (reader.has("_")) {
                explicit = true;
                reader.read("_");
            }
            property += token;
            property = uncapitalize(property);
            String getterMethodName = "get" + capitalize(property);
            final Method getterMethod = ReflectionUtils.findMethod(context, getterMethodName);
            if (getterMethod != null && !void.class.equals(getterMethod.getReturnType())) {
                path += "." + property;
                property = "";
                context = getterMethod.getReturnType();
                continue;
            }
            final Field field = ReflectionUtils.findField(context, property);
            if (field != null) {
                path += "." + property;
                property = "";
                context = field.getType();
                continue;
            }
            if (explicit) {
                throw new IllegalStateException("Could not find property " + property + " on " + context);
            }
        }
        if (!property.isEmpty()) {
            throw new IllegalStateException("Could not find property " + property + " on " + context);
        }
        path = path.substring(1);
        return new ImmutablePropertyDescriptor(path, context);
    }

    private static String uncapitalize(String expression) {
        return expression.substring(0, 1).toLowerCase() + expression.substring(1);
    }

    private static String capitalize(String expression) {
        return expression.substring(0, 1).toUpperCase() + expression.substring(1);
    }

}
