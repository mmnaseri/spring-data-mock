package com.mmnaseri.utils.spring.data.sample.repositories;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:09 PM)
 */
public interface ExtendedSimplePersonRepository extends SimplePersonRepository {

    String getString();

    Number getNumber();

}
