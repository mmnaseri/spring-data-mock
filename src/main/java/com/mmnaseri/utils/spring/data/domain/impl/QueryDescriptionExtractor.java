package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.*;
import com.mmnaseri.utils.spring.data.query.impl.*;
import com.mmnaseri.utils.spring.data.string.DocumentReader;
import com.mmnaseri.utils.spring.data.string.impl.DefaultDocumentReader;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class QueryDescriptionExtractor {

    public static final String ALL_IGNORE_CASE_SUFFIX = "(AllIgnoreCase|AllIgnoresCase|AllIgnoringCase)$";
    public static final String IGNORE_CASE_SUFFIX = "(IgnoreCase|IgnoresCase|IgnoringCase)$";
    public static final String ASC_SUFFIX = "Asc";
    public static final String DESC_SUFFIX = "Desc";
    public static final String DEFAULT_OPERATOR_SUFFIX = "Is";

    private final OperatorContext operatorContext;

    public QueryDescriptionExtractor(OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, Method method, RepositoryFactoryConfiguration configuration) {
        String methodName = method.getName();
        //check to see if the AllIgnoreCase flag is set
        boolean allIgnoreCase = methodName.matches(ALL_IGNORE_CASE_SUFFIX);
        //we need to unify method name afterwards
        methodName = allIgnoreCase ? methodName.replaceFirst(ALL_IGNORE_CASE_SUFFIX, "") : methodName;
        //create a document reader for processing method name
        final DocumentReader reader = new DefaultDocumentReader(methodName);
        //the first word in the method name is the function name
        String function = reader.read(Pattern.compile("^[a-z]+"));
        if (function == null) {
            throw new IllegalStateException("Malformed query method name: " + method);
        }
        //if the method name is one of the following, it is a simple read, and no function is required
        if (Arrays.asList("read", "find", "query", "get", "load", "select").contains(function)) {
            function = null;
        }
        //this is the limit set on the number of items being returned
        int limit = 0;
        //this is the flag that determines whether or not the result should be sifted for distinct values
        boolean distinct = false;
        //this is the extractor used for getting paging data
        final PageParameterExtractor pageExtractor;
        //this is the extractor used for getting sorting data
        final SortParameterExtractor sortExtractor;
        //these are decision branches, each of which denoting an AND clause
        final List<List<Parameter>> branches = new ArrayList<List<Parameter>>();
        //if the method name simply was the function name, no metadata can be extracted
        if (!reader.hasMore()) {
            pageExtractor = null;
            sortExtractor = null;
        } else {
            //scan for words prior to 'By'
            while (reader.hasMore() && !reader.has("By")) {
                //if the next word is Top, then we are setting a limit
                if (reader.has("First")) {
                    if (limit > 0) {
                        throw new IllegalStateException("There is already a limit of " + limit + " specified for this query: " + method);
                    }
                    reader.expect("First");
                    if (reader.has("\\d+")) {
                        limit = Integer.parseInt(reader.expect("\\d+"));
                    } else {
                        limit = 1;
                    }
                    continue;
                } else if (reader.has("Top")) {
                    if (limit > 0) {
                        throw new IllegalStateException("There is already a limit of " + limit + " specified for this query: " + method);
                    }
                    reader.expect("Top");
                    limit = Integer.parseInt(reader.expect("\\d+"));
                    continue;
                } else if (reader.has("Distinct")) {
                    //if the next word is 'Distinct', we are saying we should return distinct results
                    if (distinct) {
                        throw new IllegalStateException("You have already stated that this query should return distinct items: " + method);
                    }
                    distinct = true;
                }
                //we read the words until we reach "By".
                reader.expect("[A-Z][a-z]+");
            }
            reader.expect("By");
            if (!reader.hasMore()) {
                throw new IllegalStateException("Query method name cannot end with `By`");
            }
            int index = 0;
            branches.add(new LinkedList<Parameter>());
            while (reader.hasMore()) {
                //read a full expression
                final Parameter parameter;
                String expression = reader.expect("(.*?)(And[A-Z]|Or[A-Z]|$)");
                if (expression.matches(".*?(And|Or)[A-Z]")) {
                    //if the expression ended in And/Or, we need to put the one extra character we scanned back
                    //we scan one extra character because we don't want anything like "Order" to be mistaken for "Or"
                    reader.backtrack(1);
                    expression = expression.substring(0, expression.length() - 1);
                }
                //if the expression ended in Or, this is the end of this branch
                boolean branchEnd = expression.endsWith("Or");
                //if the expression contains an OrderBy, it is not only the end of the branch, but also the end of the query
                boolean expressionEnd = expression.matches(".+[a-z]OrderBy[A-Z].+");
                if (expressionEnd) {
                    //if that is the case, we need to put back the entirety of the order by clause
                    int length = expression.length();
                    expression = expression.replaceFirst("^(.+[a-z])OrderBy[A-Z].+$", "$1");
                    length -= expression.length();
                    reader.backtrack(length);
                }
                final Set<Modifier> modifiers = new HashSet<Modifier>();
                if (expression.matches(IGNORE_CASE_SUFFIX)) {
                    //if the expression ended in IgnoreCase, we need to strip that off
                    modifiers.add(Modifier.IGNORE_CASE);
                    expression = expression.replaceFirst(IGNORE_CASE_SUFFIX, "");
                } else if (allIgnoreCase) {
                    //if we had already set "AllIgnoreCase", we will still add the modifier
                    modifiers.add(Modifier.IGNORE_CASE);
                }
                //if the expression ends in And/Or, we expect there to be more
                if (expression.matches(".*?(And|Or)$")) {
                    if (!reader.hasMore()) {
                        throw new IllegalStateException("Expected more tokens to follow AND/OR operator");
                    }
                }
                expression = expression.replaceFirst("(And|Or)$", "");
                String property = null;
                Operator operator = null;
                //let's find out the operator that covers the longest suffix of the operation
                for (int i = 1; i < expression.length(); i++) {
                    operator = operatorContext.getBySuffix(expression.substring(i));
                    if (operator != null) {
                        property = expression.substring(0, i);
                        break;
                    }
                }
                //if no operator was found, it is the implied "IS" operator
                if (operator == null || property.isEmpty()) {
                    property = expression;
                    operator = operatorContext.getBySuffix(DEFAULT_OPERATOR_SUFFIX);
                }
                //let's get the property descriptor
                final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(repositoryMetadata.getEntityType(), property);
                property = propertyDescriptor.getPath();
                //we need to match the method parameters with the operands for the designated operator
                final int[] indices = new int[operator.getOperands()];
                for (int i = 0; i < operator.getOperands(); i++) {
                    if (index >= method.getParameterTypes().length) {
                        throw new IllegalStateException("Expected to see parameter with index " + index);
                    }
                    if (!propertyDescriptor.getType().isAssignableFrom(method.getParameterTypes()[index])) {
                        throw new IllegalStateException("Expected parameter " + index + " on method " + methodName + " to be a descendant of " + propertyDescriptor.getType());
                    }
                    indices[i] = index ++;
                }
                //create a parameter definition for the given expression
                parameter = new ImmutableParameter(property, modifiers, indices, operator);
                //get the current branch
                final List<Parameter> currentBranch = branches.get(branches.size() - 1);
                //add this parameter to the latest branch
                currentBranch.add(parameter);
                //if the branch has ended with "OR", we set up a new branch
                if (branchEnd) {
                    branches.add(new LinkedList<Parameter>());
                }
                //if this is the end of expression, so we need to jump out
                if (expressionEnd) {
                    break;
                }
            }
            final com.mmnaseri.utils.spring.data.query.Sort sort;
            //let's figure out if there is a sort requirement embedded in the query definition
            if (reader.read("OrderBy") != null) {
                final List<Order> orders = new ArrayList<Order>();
                while (reader.hasMore()) {
                    String expression = reader.expect(".*?(Asc|Desc)");
                    final SortDirection direction;
                    if (expression.endsWith(ASC_SUFFIX)) {
                        direction = SortDirection.ASCENDING;
                        expression = expression.substring(0, expression.length() - ASC_SUFFIX.length());
                    } else {
                        direction = SortDirection.DESCENDING;
                        expression = expression.substring(0, expression.length() - DESC_SUFFIX.length());
                    }
                    final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(repositoryMetadata.getEntityType(), expression);
                    if (!Comparable.class.isAssignableFrom(propertyDescriptor.getType())) {
                        throw new IllegalStateException("Sort property " + propertyDescriptor.getPath() + " is not comparable " + method);
                    }
                    final Order order = new ImmutableOrder(direction, propertyDescriptor.getPath(), NullHandling.DEFAULT);
                    orders.add(order);
                }
                sort = new ImmutableSort(orders);
            } else {
                sort = null;
            }
            if (reader.hasMore()) {
                throw new IllegalStateException("Too many tokens in the query name: " + method);
            }
            if (method.getParameterTypes().length == index) {
                pageExtractor = null;
                sortExtractor = sort == null ? null : new WrappedSortParameterExtractor(sort);
            } else if (method.getParameterTypes().length == index + 1) {
                if (Pageable.class.isAssignableFrom(method.getParameterTypes()[index])) {
                    pageExtractor = new PageablePageParameterExtractor(index);
                    sortExtractor = sort == null ? new PageableSortParameterExtractor(index) : new WrappedSortParameterExtractor(sort);
                } else if (Sort.class.isAssignableFrom(method.getParameterTypes()[index])) {
                    if (sort != null) {
                        throw new IllegalStateException("You cannot specify both an order-by clause and a dynamic ordering");
                    }
                    pageExtractor = null;
                    sortExtractor = new DirectSortParameterExtractor(index);
                } else {
                    throw new IllegalStateException("Invalid last argument: expected paging or sorting " + method);
                }
            } else {
                throw new IllegalStateException("Too many parameters declared for query method " + method);
            }
        }
        return new DefaultQueryDescriptor(distinct, function, limit, pageExtractor, sortExtractor, branches, configuration, repositoryMetadata);
    }

}
