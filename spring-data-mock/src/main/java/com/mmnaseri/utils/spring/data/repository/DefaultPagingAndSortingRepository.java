package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.impl.PropertyComparator;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import com.mmnaseri.utils.spring.data.query.impl.ImmutableOrder;
import com.mmnaseri.utils.spring.data.query.impl.ImmutableSort;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
@SuppressWarnings("WeakerAccess")
public class DefaultPagingAndSortingRepository implements DataStoreAware {

    private static final Log log = LogFactory.getLog(DefaultPagingAndSortingRepository.class);

    private DataStore dataStore;

    /**
     * Finds everything and sorts it using the given sort property
     * @param sort    how to sort the data
     * @return sorted entries, unless sort is null.
     */
    public List findAll(Sort sort) {
        log.info("Loading all the data in the data store");
        //noinspection unchecked
        final List list = new LinkedList(dataStore.retrieveAll());
        if (sort == null) {
            log.info("No sort was specified, so we are just going to return the data as-is");
            return list;
        }
        final List<Order> orders = new LinkedList<>();
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
                    break;
            }
            final Order derivedOrder = new ImmutableOrder(direction, order.getProperty(), nullHandling);
            orders.add(derivedOrder);
        }
        log.info("Sorting the retrieved data: " + orders);
        PropertyComparator.sort(list, new ImmutableSort(orders));
        return list;
    }

    /**
     * Loads everything, sorts them, and pages the according to the spec.
     * @param pageable    the pagination and sort spec
     * @return the specified view of the data
     */
    public Page findAll(Pageable pageable) {
        final List<?> all;
        if (pageable.getSort() != null) {
            log.info("The page specification requests sorting, so we are going to sort the data first");
            all = findAll(pageable.getSort());
        } else {
            log.info("The page specification does not need sorting, so we are going to load the data as-is");
            //noinspection unchecked
            all = new LinkedList(dataStore.retrieveAll());
        }
        int start = Math.max(0, pageable.getPageNumber() * pageable.getPageSize());
        int end = start + pageable.getPageSize();
        start = Math.min(start, all.size());
        end = Math.min(end, all.size());
        log.info("Trimming the selection down for page " + pageable.getPageNumber() + " to include items from " + start + " to " + end);
        final List<?> selection = new LinkedList<>(all.subList(start, end));
        //noinspection unchecked
        return new PageImpl(selection, pageable, all.size());
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
}
