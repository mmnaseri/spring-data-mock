package com.mmnaseri.utils.spring.data.domain.impl;

import org.testng.annotations.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class ImmutableParameterTest {

    @Test
    public void testToString() throws Exception {
        final ImmutableParameter parameter = new ImmutableParameter("x.y.z", Collections.emptySet(), new int[]{2, 3, 4}, new ImmutableOperator("op", 0, null));
        assertThat(parameter.toString(), is("(x.y.z,op,[2, 3, 4],[])"));
    }
}