package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.Page;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.query.Sort;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class SelectDataStoreOperation<K extends Serializable, E> implements DataStoreOperation<List<E>, K, E> {

    private final QueryDescriptor descriptor;

    public SelectDataStoreOperation(QueryDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public QueryDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public List<E> execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation) {
        final List<E> selection = new LinkedList<E>();
        final Collection<E> all = new LinkedList<E>(store.retrieveAll());
        for (E entity : all) {
            if (descriptor.matches(entity, invocation)) {
                selection.add(entity);
            }
        }
        if (descriptor.isDistinct()) {
            final Set<E> distinctValues = new HashSet<>(selection);
            selection.clear();
            selection.addAll(distinctValues);
        }
        final Sort sort = descriptor.getSort(invocation);
        final Page page = descriptor.getPage(invocation);
        if (sort != null) {
            PropertyComparator.sort(selection, sort);
        }
        if (page != null) {
            int start = page.getPageSize() * page.getPageNumber();
            int finish = Math.min(start + page.getPageSize(), selection.size());
            if (start > selection.size()) {
                selection.clear();
            } else {
                final List<E> view = new ArrayList<>();
                for (E item : selection.subList(start, finish)) {
                    view.add(item);
                }
                selection.clear();
                selection.addAll(view);
            }
        }
        if (descriptor.getLimit() > 0) {
            final List<E> view = new ArrayList<>();
            for (E item : selection.subList(0, Math.min(selection.size(), descriptor.getLimit()))) {
                view.add(item);
            }
            selection.clear();
            selection.addAll(view);
        }
        return selection;
    }

    @Override
    public String toString() {
        return descriptor.toString();
    }

}
