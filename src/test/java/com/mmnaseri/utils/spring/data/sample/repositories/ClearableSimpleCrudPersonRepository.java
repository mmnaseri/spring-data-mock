package com.mmnaseri.utils.spring.data.sample.repositories;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:47 PM)
 */
public interface ClearableSimpleCrudPersonRepository extends SimpleCrudPersonRepository {

    void clearRepo();

}
