package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.sample.mocks.DuplicatingKeyGenerator;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 3:28 PM)
 */
public class AbstractRandomKeyGeneratorTest {

    @Test
    public void testThatGeneratorProtectsUsFromDuplicates() {
        final Set<Integer> generatedKeys = new HashSet<>();
        final KeyGenerator<Integer> generator = new DuplicatingKeyGenerator();
        for (int i = 0; i < 1000; i++) {
            final Integer key = generator.generate();
            assertThat(generatedKeys, not(hasItem(key)));
            generatedKeys.add(key);
        }
    }

}