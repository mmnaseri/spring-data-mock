package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractCollectionMatcher extends AbstractSimpleMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object actual, Object property) {
        if (property == null) {
            throw new IllegalArgumentException("Comparison property cannot be null: " + parameter.getPath());
        }
        final Collection collection;
        if (property.getClass().isArray()) {
            collection = new LinkedList();
            for (int i = 0; i < Array.getLength(property); i++) {
                final Object item = Array.get(property, i);
                //noinspection unchecked
                collection.add(item);
            }
        } else if (property instanceof Iterator) {
            collection = new LinkedList();
            final Iterator iterator = (Iterator) property;
            while (iterator.hasNext()) {
                //noinspection unchecked
                collection.add(iterator.next());
            }
        } else if (property instanceof Iterable) {
            collection = new LinkedList();
            for (Object item : ((Iterable) property)) {
                //noinspection unchecked
                collection.add(item);
            }
        } else {
            throw new IllegalArgumentException("Expected an array, an iterator, or an iterable object");
        }
        return matches(parameter, actual, collection);
    }

    protected abstract boolean matches(Parameter parameter, Object actual, Collection collection);

}
