package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
@SuppressWarnings("WeakerAccess")
public class DefaultPagingAndSortingRepository extends PagingAndSortingSupport implements DataStoreAware {

    private static final Log log = LogFactory.getLog(DefaultPagingAndSortingRepository.class);

    private DataStore dataStore;

    /**
     * Finds everything and sorts it using the given sort property
     *
     * @param sort how to sort the data
     * @return sorted entries, unless sort is null.
     */
    public List findAll(Sort sort) {
        return PagingAndSortingUtils.sort(retrieveAll(), sort);
    }

    /**
     * Loads everything, sorts them, and pages the according to the spec.
     *
     * @param pageable the pagination and sort spec
     * @return the specified view of the data
     */
    public Page findAll(Pageable pageable) {
        return page(retrieveAll(), pageable);
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private Collection retrieveAll() {
        log.info("Loading all the data in the data store");
        return dataStore.retrieveAll();
    }

}
