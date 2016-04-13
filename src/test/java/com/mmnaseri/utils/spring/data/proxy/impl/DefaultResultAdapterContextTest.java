package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingResultAdapter;
import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultResultAdapterContextTest {

    @Test(expectedExceptions = ResultAdapterFailureException.class)
    public void testInvalidConversion() throws Exception {
        final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
        context.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPerson"), new Object[]{}), new Address());
    }

    @Test
    public void testRegisteringOrderedAdapters() throws Exception {
        final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
        final AtomicLong counter = new AtomicLong();
        final Person person = new Person();
        final SpyingResultAdapter first = new SpyingResultAdapter(-10000, counter, false, null);
        final SpyingResultAdapter second = new SpyingResultAdapter(10000, counter, true, person);
        context.register(first);
        context.register(second);
        final Object result = context.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPerson"), new Object[]{}), new Address());
        assertThat(first.getCheck(), is(notNullValue()));
        assertThat(second.getCheck(), is(notNullValue()));
        assertThat(first.getCheck(), is(lessThan(second.getCheck())));
        assertThat(first.getRequest(), is(nullValue()));
        assertThat(second.getCheck(), is(lessThan(second.getRequest())));
        assertThat(result, Matchers.<Object>is(person));
    }

}