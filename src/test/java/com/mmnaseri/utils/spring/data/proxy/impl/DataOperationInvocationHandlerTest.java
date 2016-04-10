package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.model.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.testng.annotations.Test;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DataOperationInvocationHandlerTest {

    @SuppressWarnings("WeakerAccess")
    public interface SampleEntityRepository extends JpaRepository<SampleEntity, String> {
    }

    /**
     * Regression test to reproduce #12
     * @throws Exception
     */
    @Test
    public void testCallingHashCode() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);
        Integer result = repository.hashCode();
        assertThat(result, is(notNullValue()));
    }

    /**
     * Regression test to reproduce #12
     * @throws Exception
     */
    @Test
    public void testCallingEquals() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);
        //noinspection EqualsWithItself
        Boolean result = repository.equals(repository);
        assertThat(result, is(true));
    }

    /**
     * Regression test to reproduce #12
     * @throws Exception
     */
    @Test
    public void testCallingToString() throws Exception {
        final SampleEntityRepository repository = builder().mock(SampleEntityRepository.class);
        String result = repository.toString();
        assertThat(result, is(notNullValue()));
    }

}
