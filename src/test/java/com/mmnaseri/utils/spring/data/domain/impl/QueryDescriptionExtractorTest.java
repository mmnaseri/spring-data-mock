package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.error.QueryParserException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
public class QueryDescriptionExtractorTest {

    private QueryDescriptionExtractor extractor;
    private RepositoryMetadata malformedRepositoryMetadata;
    private RepositoryMetadata sampleRepositoryMetadata;
    private RepositoryFactoryConfiguration configuration;

    @BeforeMethod
    public void setUp() throws Exception {
        extractor = new QueryDescriptionExtractor(new DefaultOperatorContext());
        malformedRepositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, MalformedRepository.class, "id");
        sampleRepositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, SampleRepository.class, "id");
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Malformed query method name.*")
    public void testMethodNameNotStartingWithNormalWord() throws Exception {
        configuration = RepositoryFactoryBuilder.defaultConfiguration();
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("Malformed"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: There is already a limit of 10 specified for this query:.*")
    public void testMultipleLimits() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findTop10Top5"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: You have already stated that this query should return distinct items:.*")
    public void testMultipleDistinctFlags() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findDistinctDistinct"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Expected pattern 'By' was not encountered.*")
    public void testNonSimpleQueryWithoutBy() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findTop10Distinct"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Query method name cannot end with `By`")
    public void testNonSimpleQueryEndingInBy() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findTop10DistinctBy"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Could not find property `unknownProperty`.*")
    public void testUnknownPropertyInExpression() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByUnknownProperty"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Expected to see parameter with index 0")
    public void testTooFewParameterNumber() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstName"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Expected parameter 0 on .*? to be a descendant of class .*?\\.String")
    public void testBadParameterType() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstName", Object.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Invalid last argument: expected paging or sorting.*?")
    public void testBadLastParameter() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstName", String.class, Object.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Too many parameters.*?")
    public void testTooManyParameters() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstName", String.class, Object.class, Object.class), configuration);
    }

    @Test
    public void testReadMethodWithoutAnyCriteria() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("find"), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getConfiguration(), is(configuration));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        assertThat(descriptor.getRepositoryMetadata(), is(sampleRepositoryMetadata));
        assertThat(descriptor.getBranches(), is(empty()));
    }

    @Test
    public void testCustomFunctionWithoutAnyCriteria() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("test"), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getConfiguration(), is(configuration));
        assertThat(descriptor.getFunction(), is("test"));
        assertThat(descriptor.getLimit(), is(0));
        assertThat(descriptor.getRepositoryMetadata(), is(sampleRepositoryMetadata));
        assertThat(descriptor.getBranches(), is(empty()));
    }

    @Test
    public void testQueryMethodWithSingleBranch() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameAndLastNameEquals", String.class, String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getConfiguration(), is(configuration));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        assertThat(descriptor.getRepositoryMetadata(), is(sampleRepositoryMetadata));
        assertThat(descriptor.getBranches(), hasSize(1));
        final List<Parameter> parameters = descriptor.getBranches().get(0);
        assertThat(parameters, hasSize(2));
        assertThat(parameters.get(0).getIndices().length, is(1));
        assertThat(parameters.get(0).getIndices()[0], is(0));
        assertThat(parameters.get(0).getModifiers(), is(empty()));
        assertThat(parameters.get(0).getPath(), is("firstName"));
        assertThat(parameters.get(0).getOperator().getName(), is("IS"));
        assertThat(parameters.get(1).getIndices().length, is(1));
        assertThat(parameters.get(1).getIndices()[0], is(1));
        assertThat(parameters.get(1).getModifiers(), is(empty()));
        assertThat(parameters.get(1).getPath(), is("lastName"));
        assertThat(parameters.get(1).getOperator().getName(), is("IS"));
    }

    @Test
    public void testModifierOnSingleParameter() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameAndLastNameIgnoreCase", String.class, String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getConfiguration(), is(configuration));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        assertThat(descriptor.getRepositoryMetadata(), is(sampleRepositoryMetadata));
        assertThat(descriptor.getBranches(), hasSize(1));
        final List<Parameter> parameters = descriptor.getBranches().get(0);
        assertThat(parameters, hasSize(2));
        assertThat(parameters.get(0).getIndices().length, is(1));
        assertThat(parameters.get(0).getIndices()[0], is(0));
        assertThat(parameters.get(0).getModifiers(), is(empty()));
        assertThat(parameters.get(0).getPath(), is("firstName"));
        assertThat(parameters.get(0).getOperator().getName(), is("IS"));
        assertThat(parameters.get(1).getIndices().length, is(1));
        assertThat(parameters.get(1).getIndices()[0], is(1));
        assertThat(parameters.get(1).getModifiers(), is(not(empty())));
        assertThat(parameters.get(1).getModifiers(), contains(Modifier.IGNORE_CASE));
        assertThat(parameters.get(1).getPath(), is("lastName"));
        assertThat(parameters.get(1).getOperator().getName(), is("IS"));
    }

    @SuppressWarnings("unused")
    private interface MalformedRepository {

        void Malformed();

        void findTop10Top5();

        void findDistinctDistinct();

        void findTop10Distinct();

        void findTop10DistinctBy();

        void findByUnknownProperty();

        void findByFirstName();

        void findByFirstName(Object name);

        void findByFirstName(String name, Object extra);

        void findByFirstName(String name, Object first, Object second);

    }

    @SuppressWarnings("unused")
    private interface SampleRepository {

        void find();

        void test();

        void findByFirstNameAndLastNameEquals(String firstName, String lastName);

        void findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);

    }

}