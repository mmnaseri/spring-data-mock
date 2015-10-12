package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.dsl.config.RepositoryFactoryConfigurationBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
public class QueryDescriptionExtractorTest {

    private QueryDescriptionExtractor extractor;
    private RepositoryMetadata repositoryMetadata;
    private RepositoryFactoryConfiguration configuration;

    @BeforeMethod
    public void setUp() throws Exception {
        extractor = new QueryDescriptionExtractor(new DefaultOperatorContext());
        repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, MalformedRepository.class, "id");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Malformed query method name.*")
    public void testMethodNameNotStartingWithNormalWord() throws Exception {
        configuration = RepositoryFactoryConfigurationBuilder.defaultConfiguration();
        extractor.extract(repositoryMetadata, MalformedRepository.class.getMethod("Malformed"), configuration);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "There is already a limit of 10 specified for this query:.*")
    public void testMultipleLimits() throws Exception {
        extractor.extract(repositoryMetadata, MalformedRepository.class.getMethod("findTop10Top5"), configuration);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "You have already stated that this query should return distinct items:.*")
    public void testMultipleDistinctFlags() throws Exception {
        extractor.extract(repositoryMetadata, MalformedRepository.class.getMethod("findDistinctDistinct"), configuration);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Expected pattern 'By' was not encountered")
    public void testNonSimpleQueryWithoutBy() throws Exception {
        extractor.extract(repositoryMetadata, MalformedRepository.class.getMethod("findTop10Distinct"), configuration);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Query method name cannot end with `By`")
    public void testNonSimpleQueryEndingInBy() throws Exception {
        extractor.extract(repositoryMetadata, MalformedRepository.class.getMethod("findTop10DistinctBy"), configuration);
    }

}