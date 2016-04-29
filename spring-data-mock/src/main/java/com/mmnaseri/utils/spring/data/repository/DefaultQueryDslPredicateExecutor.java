package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.querydsl.collections.CollQuery;
import com.querydsl.collections.CollQueryFactory;
import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/28/16)
 */
public class DefaultQueryDslPredicateExecutor extends PagingAndSortingSupport implements DataStoreAware {

    private DataStore dataStore;
    private Object alias;

    public Object findOne(Predicate predicate) {
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetchOne();
    }

    public Iterable findAll(Predicate predicate) {
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetch();
    }

    public Iterable findAll(Predicate predicate, Sort sort) {
        return sort(((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetch(), sort);
    }

    public Page findAll(Predicate predicate, Pageable pageable) {
        return page(((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetch(), pageable);
    }

    public long count(Predicate predicate) {
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetchCount();
    }

    public boolean exists(Predicate predicate) {
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate)).fetchCount() > 0;
    }

    public Iterable findAll(OrderSpecifier... orders) {
        //noinspection unchecked
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).orderBy(orders)).fetch();
    }

    public Iterable findAll(Predicate predicate, OrderSpecifier... orders) {
        //noinspection unchecked
        return ((CollQuery) CollQueryFactory.from(alias, dataStore.retrieveAll()).where(predicate).orderBy(orders)).fetch();
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
        this.alias = Alias.alias(dataStore.getEntityType());
    }

}
