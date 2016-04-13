package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class paves the way for matching a value against a collection of items
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractCollectionMatcher extends AbstractSimpleMatcher {

    @Override
    protected final boolean matches(Parameter parameter, Object actual, Object property) {
        if (property == null) {
            throw new InvalidArgumentException("Comparison property cannot be null: " + parameter.getPath());
        }
        final Collection collection;
        if (property.getClass().isArray()) {
            // if it was an array, we do array to collection conversion, which is pretty straightforward
            collection = new LinkedList();
            for (int i = 0; i < Array.getLength(property); i++) {
                final Object item = Array.get(property, i);
                //noinspection unchecked
                collection.add(item);
            }
        } else if (property instanceof Iterator) {
            // if it is an iterator, we just iterate it forward
            collection = new LinkedList();
            final Iterator iterator = (Iterator) property;
            while (iterator.hasNext()) {
                //noinspection unchecked
                collection.add(iterator.next());
            }
        } else if (property instanceof Iterable) {
            //if it is already an iterable object, we just iterate over it.
            collection = new LinkedList();
            for (Object item : ((Iterable) property)) {
                //noinspection unchecked
                collection.add(item);
            }
        } else {
            //otherwise, we just don't know how to convert it!
            throw new InvalidArgumentException("Expected an array, an iterator, or an iterable object");
        }
        return matches(parameter, actual, collection);
    }

    /**
     * Used to find out if a collection satisfies the condition set forth by this matcher
     * @param parameter     the parameter
     * @param actual        the actual value
     * @param collection    the collection
     * @return {@literal true} if the match was a success
     */
    protected abstract boolean matches(Parameter parameter, Object actual, Collection collection);

}
