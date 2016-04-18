package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.Iterator;

/**
 * <p>This class will adapt results from an iterable object to a number. The future task returned will have already
 * executed with the results available.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link Number}. Additionally, it will check to see if the iterable yields only one item and
 * also that the item is a number.</p>
 *
 * <p>Here is a list of supported number types:</p>
 *
 * <ul>
 *     <li>{@link Long}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Byte}</li>
 *     <li>{@link Double}</li>
 *     <li>{@link Float}</li>
 * </ul>
 *
 * <p>This adapter will execute at priority {@literal -425}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public class NumberIterableResultAdapter extends AbstractIterableResultAdapter<Object> {

    public NumberIterableResultAdapter() {
        super(-425);
    }

    @Override
    protected Object doAdapt(Invocation invocation, Iterable iterable) {
        final Iterator iterator = iterable.iterator();
        final Object value = iterator.next();
        final Number number = (Number) value;
        final Class<?> returnType = PropertyUtils.getTypeOf(invocation.getMethod().getReturnType());
        if (Long.class.equals(returnType)) {
            return number.longValue();
        } else if (Short.class.equals(returnType)) {
            return number.shortValue();
        } else if (Integer.class.equals(returnType)) {
            return number.intValue();
        } else if (Byte.class.equals(returnType)) {
            return number.byteValue();
        } else if (Double.class.equals(returnType)) {
            return number.doubleValue();
        } else if (Float.class.equals(returnType)) {
            return number.floatValue();
        }
        throw new ResultAdapterFailureException(value, returnType);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        if (originalValue == null) {
            return false;
        }
        if (!Number.class.isAssignableFrom(PropertyUtils.getTypeOf(invocation.getMethod().getReturnType()))) {
            return false;
        }
        if (originalValue instanceof Iterable) {
            Iterable iterable = (Iterable) originalValue;
            final Iterator iterator = iterable.iterator();
            if (iterator.hasNext()) {
                final Object value = iterator.next();
                return value instanceof Number && !iterator.hasNext();
            }
        }
        return false;
    }

}
