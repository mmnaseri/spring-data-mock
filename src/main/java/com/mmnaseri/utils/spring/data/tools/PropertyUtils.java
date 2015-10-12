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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public abstract class PropertyUtils {

    private static final Map<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();

    static {
        types.put(int.class, Integer.class);
        types.put(short.class, Short.class);
        types.put(long.class, Long.class);
        types.put(double.class, Double.class);
        types.put(char.class, Character.class);
        types.put(float.class, Float.class);
        types.put(boolean.class, Boolean.class);
        types.put(byte.class, Byte.class);
    }

    public static Object getPropertyValue(Object context, String property) {
        final BeanWrapper wrapper = new BeanWrapperImpl(context);
        return wrapper.getPropertyValue(property);
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> domainType, String expression) {
        final String followUp;
        if (expression.contains("_")) {
            final String[] split = expression.split("_", 2);
            expression = split[0];
            followUp = split[1];
        } else {
            followUp = "";
        }
        Class<?> context = domainType;
        final DocumentReader reader = new DefaultDocumentReader(expression);
        final List<String> tokens = new ArrayList<String>();
        while (reader.hasMore()) {
            tokens.add(reader.expect("[A-Z][a-z]*"));
        }
        int cursor = 0;
        final StringBuilder path = new StringBuilder();
        while (cursor < tokens.size()) {
            boolean found = false;
            for (int i = tokens.size(); i >= cursor; i--) {
                final String propertyName = getPropertyName(tokens, cursor, i);
                final Method getter = ReflectionUtils.findMethod(context, "get" + StringUtils.capitalize(propertyName));
                if (getter != null) {
                    context = getter.getReturnType();
                    cursor = i;
                    found = true;
                    path.append(".").append(propertyName);
                    break;
                }
                final Field field = ReflectionUtils.findField(context, propertyName);
                if (field != null) {
                    context = field.getType();
                    cursor = i;
                    found = true;
                    path.append(".").append(propertyName);
                    break;
                }
            }
            if (!found) {
                throw new IllegalStateException("Could not find property `" + getPropertyName(tokens, cursor, tokens.size()) + "` on `" + context + "`");
            }
        }
        if (!followUp.isEmpty()) {
            final PropertyDescriptor descriptor = getPropertyDescriptor(context, followUp);
            return new ImmutablePropertyDescriptor(path.substring(1) + "." + descriptor.getPath(), descriptor.getType());
        }
        return new ImmutablePropertyDescriptor(path.substring(1), context);
    }
    
    private static String getPropertyName(List<String> tokens, int from, int to) {
        final StringBuilder builder = new StringBuilder();
        for (int i = from; i < to; i++) {
            builder.append(tokens.get(i));
        }
        return StringUtils.uncapitalize(builder.toString());
    }

    public static Class<?> getTypeOf(Class<?> type) {
        if (type.isPrimitive()) {
            return types.get(type);
        } else {
            return type;
        }
    }

}
