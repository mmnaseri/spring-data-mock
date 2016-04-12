package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.error.QueryParserException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        configuration = new DefaultRepositoryFactoryConfiguration();
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("Malformed"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: There is already a limit of 5 specified for this query:.*")
    public void testMultipleLimitsUsingFirst() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findFirst5First10"), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: There is already a limit of 1 specified for this query:.*")
    public void testMultipleLimitsUsingFirstOne() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findFirstFirst10"), configuration);
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

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Expected more tokens to follow AND/OR operator")
    public void testTooFewExpressionEndingInOr() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOr", String.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Expected more tokens to follow AND/OR operator")
    public void testTooFewExpressionEndingInAnd() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOr", String.class), configuration);
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

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Could not find property `firstNameOrderBy` on `class com.mmnaseri.utils.spring.data.domain.model.Person`")
    public void testWithTrailingOrderBy() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOrderBy", String.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Failed to get a property descriptor for expression: Xyz")
    public void testWithOrderByInvalidProperty() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOrderByXyzDesc", String.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: You cannot specify both an order-by clause and a dynamic ordering")
    public void testWithMultipleOrders() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOrderByFirstNameAsc", String.class, org.springframework.data.domain.Sort.class), configuration);
    }

    @Test(expectedExceptions = QueryParserException.class, expectedExceptionsMessageRegExp = ".*?: Sort property `address` is not comparable in `findByFirstNameOrderByAddressAsc`")
    public void testWithOrderByNonComparableProperty() throws Exception {
        extractor.extract(malformedRepositoryMetadata, MalformedRepository.class.getMethod("findByFirstNameOrderByAddressAsc", String.class), configuration);
    }

    @Test
    public void testIntegrity() throws Exception {
        assertThat(extractor.getOperatorContext(), is(notNullValue()));
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

    @Test
    public void testWithOrderBy() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameOrderByLastNameDescAgeAsc", String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        final Sort sort = descriptor.getSort(new ImmutableInvocation(null, new Object[0]));
        assertThat(sort.getOrders(), hasSize(2));
        assertThat(sort.getOrders().get(0).getDirection(), is(SortDirection.DESCENDING));
        assertThat(sort.getOrders().get(0).getProperty(), is("lastName"));
        assertThat(sort.getOrders().get(1).getDirection(), is(SortDirection.ASCENDING));
        assertThat(sort.getOrders().get(1).getProperty(), is("age"));
    }

    @Test
    public void testWithMultipleBranches() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameOrLastName", String.class, String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(2));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getBranches().get(1), hasSize(1));
        assertThat(descriptor.getBranches().get(1).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(1).get(0).getIndices()[0], is(1));
        assertThat(descriptor.getBranches().get(1).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(1).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(1).get(0).getPath(), is("lastName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
    }

    @Test
    public void testWithStaticSortingAndDynamicPaging() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameOrderByLastNameDesc", String.class, Pageable.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        final List<Order> orders = descriptor.getSort(new ImmutableInvocation(null, null)).getOrders();
        assertThat(orders, hasSize(1));
        assertThat(orders.get(0).getProperty(), is("lastName"));
        assertThat(orders.get(0).getDirection(), is(SortDirection.DESCENDING));
        final Page page = descriptor.getPage(new ImmutableInvocation(null, new Object[]{null, new PageRequest(0, 1)}));
        assertThat(page, is(notNullValue()));
        assertThat(page.getPageSize(), is(1));
        assertThat(page.getPageNumber(), is(0));
    }

    @Test
    public void testWithDynamicSortingAndDynamicPaging() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstName", String.class, Pageable.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        final ImmutableInvocation invocation = new ImmutableInvocation(null, new Object[]{null, new PageRequest(0, 1, new org.springframework.data.domain.Sort(org.springframework.data.domain.Sort.Direction.ASC, "firstName", "lastName"))});
        final List<Order> orders = descriptor.getSort(invocation).getOrders();
        assertThat(orders, hasSize(2));
        assertThat(orders.get(0).getProperty(), is("firstName"));
        assertThat(orders.get(0).getDirection(), is(SortDirection.ASCENDING));
        assertThat(orders.get(1).getProperty(), is("lastName"));
        assertThat(orders.get(1).getDirection(), is(SortDirection.ASCENDING));
        final Page page = descriptor.getPage(invocation);
        assertThat(page, is(notNullValue()));
        assertThat(page.getPageSize(), is(1));
        assertThat(page.getPageNumber(), is(0));
    }

    @Test
    public void testWithDynamicSortAndNoPaging() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstName", String.class, org.springframework.data.domain.Sort.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
        final ImmutableInvocation invocation = new ImmutableInvocation(null, new Object[]{null, new org.springframework.data.domain.Sort(org.springframework.data.domain.Sort.Direction.ASC, "firstName", "lastName")});
        final List<Order> orders = descriptor.getSort(invocation).getOrders();
        assertThat(orders, hasSize(2));
        assertThat(orders.get(0).getProperty(), is("firstName"));
        assertThat(orders.get(0).getDirection(), is(SortDirection.ASCENDING));
        assertThat(orders.get(1).getProperty(), is("lastName"));
        assertThat(orders.get(1).getDirection(), is(SortDirection.ASCENDING));
        final Page page = descriptor.getPage(invocation);
        assertThat(page, is(nullValue()));
    }

    @Test
    public void testAllIgnoreCase() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("findByFirstNameAndLastNameAllIgnoreCase", String.class, String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(2));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), contains(Modifier.IGNORE_CASE));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getBranches().get(0).get(1).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(1).getIndices()[0], is(1));
        assertThat(descriptor.getBranches().get(0).get(1).getModifiers(), contains(Modifier.IGNORE_CASE));
        assertThat(descriptor.getBranches().get(0).get(1).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(1).getPath(), is("lastName"));
        assertThat(descriptor.getFunction(), is(nullValue()));
        assertThat(descriptor.getLimit(), is(0));
    }

    @Test
    public void testFunction() throws Exception {
        final QueryDescriptor descriptor = extractor.extract(sampleRepositoryMetadata, SampleRepository.class.getMethod("myFunctionByFirstName", String.class), configuration);
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getBranches(), hasSize(1));
        assertThat(descriptor.getBranches().get(0), hasSize(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices().length, is(1));
        assertThat(descriptor.getBranches().get(0).get(0).getIndices()[0], is(0));
        assertThat(descriptor.getBranches().get(0).get(0).getModifiers(), is(empty()));
        assertThat(descriptor.getBranches().get(0).get(0).getOperator().getName(), is("IS"));
        assertThat(descriptor.getBranches().get(0).get(0).getPath(), is("firstName"));
        assertThat(descriptor.getFunction(), is("myFunction"));
        assertThat(descriptor.getLimit(), is(0));
    }

    @SuppressWarnings("unused")
    private interface MalformedRepository {

        void Malformed();

        void findFirst5First10();

        void findFirstFirst10();

        void findTop10Top5();

        void findDistinctDistinct();

        void findTop10Distinct();

        void findTop10DistinctBy();

        void findByUnknownProperty();

        void findByFirstName();

        void findByFirstNameAnd(String firstName);

        void findByFirstNameOr(String firstName);

        void findByFirstName(Object name);

        void findByFirstName(String name, Object extra);

        void findByFirstName(String name, Object first, Object second);

        void findByFirstNameOrderBy(String firstName);

        void findByFirstNameOrderByAddressAsc(String firstName);

        void findByFirstNameOrderByXyzDesc(String firstName);

        void findByFirstNameOrderByFirstNameAsc(String firstName, org.springframework.data.domain.Sort sort);

    }

    @SuppressWarnings("unused")
    private interface SampleRepository {

        void find();

        void test();

        void findByFirstNameAndLastNameEquals(String firstName, String lastName);

        void findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);

        void findByFirstNameOrderByLastNameDescAgeAsc(String firstName);

        void findByFirstNameOrLastName(String firstName, String lastName);

        void findByFirstNameOrderByLastNameDesc(String firstName, Pageable pageable);

        void findByFirstName(String firstName, Pageable pageable);

        void findByFirstName(String firstName, org.springframework.data.domain.Sort sort);

        void findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);

        void myFunctionByFirstName(String firstName);

    }

}