package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.query.Order;
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

    @Override
    public List<E> execute(DataStore<K, E> store, RepositoryMetadata repositoryMetadata, Invocation invocation) {
        List<E> selection = new LinkedList<E>();
        final Collection<E> all = new LinkedList<E>(store.retrieveAll());
        for (E entity : all) {
            if (descriptor.matches(entity, invocation)) {
                selection.add(entity);
            }
        }
        if (descriptor.isDistinct()) {
            selection = new LinkedList<E>(new HashSet<E>(selection));
        }
        final Sort sort = descriptor.getSort(invocation);
        final Page page = descriptor.getPage(invocation);
        if (sort != null) {
            for (Order order : sort.getOrders()) {
                Collections.sort(selection, new PropertyComparator(order));
            }
        }
        if (page != null) {
            int start = page.getPageSize() * page.getPageNumber();
            int finish = Math.min(start + page.getPageSize(), selection.size());
            if (start > selection.size()) {
                selection = new ArrayList<E>();
            } else {
                selection = selection.subList(start, finish);
            }
        }
        if (descriptor.getLimit() > 0) {
            selection = selection.subList(0, Math.min(selection.size(), descriptor.getLimit()));
        }
        return selection;
    }

}
