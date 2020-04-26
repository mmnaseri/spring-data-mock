package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

import org.springframework.data.jpa.repository.Query;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:31 PM)
 */
public interface SampleMappedRepository {

    void mappedSignature(String string);

    void findByFirstName(String firstName);

    @Query
    void nativeMethod();

    void normalMethodBy();

}
