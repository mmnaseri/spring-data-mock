package com.mmnaseri.utils.spring.data.proxy.impl;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.testng.annotations.Test;

import com.mmnaseri.utils.spring.data.domain.model.SampleEntity;

public class DataOperationInvocationHandlerTest {

    public interface SampleEntityRepository extends JpaRepository<SampleEntity, String> {

    }

    @Test
    public void testCallingHashCode() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);

        Integer result = repository.hashCode();

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testCallingEquals() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);

        Boolean result = repository.equals(repository);

        assertThat(result, is(true));
    }

    @Test
    public void testCallingToString() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);

        String result = repository.toString();

        assertThat(result, is(notNullValue()));
    }
}
