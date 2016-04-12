package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.impl.PropertyComparator;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import com.mmnaseri.utils.spring.data.query.impl.ImmutableOrder;
import com.mmnaseri.utils.spring.data.query.impl.ImmutableSort;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class DefaultPagingAndSortingRepository implements DataStoreAware {

    private DataStore dataStore;

    public List findAll(Sort sort) {
        //noinspection unchecked
        final List list = new LinkedList(dataStore.retrieveAll());
        if (sort == null) {
            return list;
        }
        final LinkedList<Order> orders = new LinkedList<>();
        for (Sort.Order order : sort) {
            final SortDirection direction = order.getDirection().equals(Sort.Direction.ASC) ? SortDirection.ASCENDING : SortDirection.DESCENDING;
            final NullHandling nullHandling;
            switch (order.getNullHandling()) {
                case NULLS_FIRST:
                    nullHandling = NullHandling.NULLS_FIRST;
                    break;
                case NULLS_LAST:
                    nullHandling = NullHandling.NULLS_LAST;
                    break;
                default:
                    nullHandling = NullHandling.DEFAULT;
            }
            final Order derivedOrder = new ImmutableOrder(direction, order.getProperty(), nullHandling);
            orders.add(derivedOrder);
        }
        PropertyComparator.sort(list, new ImmutableSort(orders));
        return list;
    }

    public Page findAll(Pageable pageable) {
        final List<?> all;
        if (pageable.getSort() != null) {
            all = findAll(pageable.getSort());
        } else {
            //noinspection unchecked
            all = new LinkedList(dataStore.retrieveAll());
        }
        int start = Math.max(0, pageable.getPageNumber() * pageable.getPageSize());
        int end = start + pageable.getPageSize();
        start = Math.min(start, all.size());
        end = Math.min(end, all.size());
        final List<?> selection = new LinkedList<>(all.subList(start, end));
        //noinspection unchecked
        return new PageImpl(selection, pageable, all.size());
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
}
