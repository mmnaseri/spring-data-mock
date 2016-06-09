package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.error.QueryParserException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.*;
import com.mmnaseri.utils.spring.data.query.impl.*;
import com.mmnaseri.utils.spring.data.string.DocumentReader;
import com.mmnaseri.utils.spring.data.string.impl.DefaultDocumentReader;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import com.mmnaseri.utils.spring.data.tools.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>This class will parse a query method's name and extract a {@link com.mmnaseri.utils.spring.data.dsl.factory.QueryDescription query description}
 * from that name.</p>
 *
 * <p>In parsing the name, words are considered as being tokens in a camel case name.</p>
 *
 * <p>Here is how a query method's name is parsed:</p>
 *
 * <ol>
 *     <li>We will look at the first word in the name until we reach one of the keywords that says we are specifying a limit, or
 *     we are going over the criteria defined by the method. This prefix will be called the "function name" for the operation
 *     defined by the query method. If the function name is one of "read", "find", "query", "get", "load", or "select", we will
 *     set the function name to {@literal null} to indicate that no special function should be applied to the result set. We
 *     are only looking at the first word to let you be more verbose about the purpose of your query (e.g.
 *     {@literal findAllGreatPeopleByGreatnessGreaterThan(Integer greatness)} will still resolve to the function
 *     {@literal find}, which will ultimately be returned as {@literal null}</li>
 *     <li>We will then look for any of these patterns:
 *     <ul>
 *         <li>The word {@literal By}, signifying that we are ready to start parsing the query criteria</li>
 *         <li>One of the words {@literal First} or {@literal Top}, signifying that we should look for a limit on the number
 *         of results returned.</li>
 *         <li>The word {@literal Distinct}, signifying that the results should include no duplicates.</li>
 *     </ul>
 *     </li>
 *     <li>If the word {@literal First} had appeared, we will see if it is followed by an integer. If it is, that will be the limit.
 *     If not, a limit of {@literal 1} is assumed.</li>
 *     <li>If the word {@literal Top} had appeared, we will look for the limit number, which should be an integer value.</li>
 *     <li>At this point, we will continue until we see 'By'. In the above, steps, we will look for the keywords in any order,
 *     and there can be any words in between. So, {@literal getTop5StudentsWhoAreAwesomeDistinct} is the same as {@literal getTop5Distinct}</li>
 *     <li>Once we reach the word "By", we will read the query in terms of "decision branches". Branches are separated using the keyword
 *     "Or", and each branch is a series of conjunctions. So, while you are separating your conditions with "And", you are in the same branch.</li>
 *     <li>A single branch consists of the pattern: "(Property)(Operator)?((And)(Property)(Operator)?)*". If the operator is missing, "Is" is assumed.
 *     Properties must match a proper property in the domain object. So, if you have "AddressZipPrefix" in your query method name, there must be a property
 *     reachable by one of the following paths in your domain class (in the given order):
 *     <ul>
 *         <li>{@literal addressZipPrefix}</li>
 *         <li>{@literal addressZip.prefix}</li>
 *         <li>{@literal address.zipPrefix}</li>
 *         <li>{@literal address.zip.prefix}</li>
 *     </ul>
 *     Note that if you have both the "addressZip" and "address.zip" in your entity, the first will be taken up. To force the parser to choose the former, use
 *     the underscore character ({@literal _}) in place of the dot, like so: "{@literal Address_Zip}"<br>
 *     Depending on the operator that was matched to the suffix provided (e.g. GreaterThan, Is, etc.), a given number of method parameters will be matched
 *     as the operands to that operator. For instance, "Is" requires two values to determine equality, one if the property found on the domain object, and
 *     the other must be provided by the query method.<br>
 *     The operators themselves are scanned eagerly and based on the set of operators defined in the {@link OperatorContext}.
 *     </li>
 *     <li>We continue the pattern indicated above, until we reach the end of the method name, or we reach the "OrderBy" pattern. Once we see "OrderBy"
 *     we expect the following pattern: "((Property)(Direction))+", wherein "Property" must follow the same rule as above, and "Direction" is one of
 *     "Asc" and "Desc" to indicate "ascending" and "descending" ordering, respectively.</li>
 *     <li>Finally, we look to see if the keyword "AllIgnoreCase" or {@link #ALL_IGNORE_CASE_SUFFIX one of its variations} is present at the end of the
 *     query name, which will indicate all applicable comparisons should be case-insensitive.</li>
 *     <li>At the end, we allow one additional parameter for the query method, which can be of either of these types:
 *     <ul>
 *         <li>{@link Sort Sort}: to indicate a dynamic sort defined at runtime. If a static sort is already indicated via the pattern above, this will
 *         result in an error.</li>
 *         <li>{@link Pageable Pageable}: to indicate a paging (and, possibly, sorting) at runtime. If a static sort is already indicated via the pattern
 *         above, the sort portion of this parameter will be always ignored.</li>
 *     </ul>
 *     </li>
 * </ol>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class MethodQueryDescriptionExtractor implements QueryDescriptionExtractor<Method> {

    private static final String ALL_IGNORE_CASE_SUFFIX = "(AllIgnoreCase|AllIgnoresCase|AllIgnoringCase)$";
    private static final String IGNORE_CASE_SUFFIX = "(IgnoreCase|IgnoresCase|IgnoringCase)$";
    private static final String ASC_SUFFIX = "Asc";
    private static final String DESC_SUFFIX = "Desc";
    private static final String DEFAULT_OPERATOR_SUFFIX = "Is";

    private final OperatorContext operatorContext;

    public MethodQueryDescriptionExtractor(OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    /**
     * Extracts query description from a method's name. This will be done according to {@link MethodQueryDescriptionExtractor the parsing rules}
     * for this extractor.
     *
     * @param repositoryMetadata    the repository metadata for this method.
     * @param configuration         the repository factory configuration. This will be passed down through the description.
     * @param method                the query method
     * @return the description for the query
     */
    @Override
    public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, RepositoryFactoryConfiguration configuration, Method method) {
        String methodName = method.getName();
        //check to see if the AllIgnoreCase flag is set
        boolean allIgnoreCase = methodName.matches(".*" + ALL_IGNORE_CASE_SUFFIX);
        //we need to unify method name afterwards
        methodName = allIgnoreCase ? methodName.replaceFirst(ALL_IGNORE_CASE_SUFFIX, "") : methodName;
        //create a document reader for processing method name
        final DocumentReader reader = new DefaultDocumentReader(methodName);
        String function = parseFunctionName(method, reader);
        final QueryModifiers queryModifiers = parseQueryModifiers(method, reader);
        //this is the extractor used for getting paging data
        final PageParameterExtractor pageExtractor;
        //this is the extractor used for getting sorting data
        SortParameterExtractor sortExtractor = null;
        //these are decision branches, each of which denoting an AND clause
        final List<List<Parameter>> branches = new ArrayList<>();
        //if the method name simply was the function name, no metadata can be extracted
        if (!reader.hasMore()) {
            pageExtractor = null;
            sortExtractor = null;
        } else {
            reader.expect("By");
            if (!reader.hasMore()) {
                throw new QueryParserException(method.getDeclaringClass(), "Query method name cannot end with `By`");
            }
            //current parameter index
            int index = parseExpression(repositoryMetadata, method, methodName, allIgnoreCase, reader, branches);
            final com.mmnaseri.utils.spring.data.query.Sort sort = parseSort(repositoryMetadata, method, reader);
            pageExtractor = getPageParameterExtractor(method, index, sort);
            sortExtractor = getSortParameterExtractor(method, index, sort);
        }
        return new DefaultQueryDescriptor(queryModifiers.isDistinct(), function, queryModifiers.getLimit(), pageExtractor, sortExtractor, branches, configuration, repositoryMetadata);
    }

    private SortParameterExtractor getSortParameterExtractor(Method method, int index, com.mmnaseri.utils.spring.data.query.Sort sort) {
        SortParameterExtractor sortExtractor = null;
        if (method.getParameterTypes().length == index) {
            sortExtractor = sort == null ? null : new WrappedSortParameterExtractor(sort);
        } else if (method.getParameterTypes().length == index + 1) {
            if (Pageable.class.isAssignableFrom(method.getParameterTypes()[index])) {
                sortExtractor = sort == null ? new PageableSortParameterExtractor(index) : new WrappedSortParameterExtractor(sort);
            } else if (Sort.class.isAssignableFrom(method.getParameterTypes()[index])) {
                sortExtractor = new DirectSortParameterExtractor(index);
            }
        }
        return sortExtractor;
    }

    private PageParameterExtractor getPageParameterExtractor(Method method, int index, com.mmnaseri.utils.spring.data.query.Sort sort) {
        PageParameterExtractor pageExtractor;
        if (method.getParameterTypes().length == index) {
            pageExtractor = null;
        } else if (method.getParameterTypes().length == index + 1) {
            if (Pageable.class.isAssignableFrom(method.getParameterTypes()[index])) {
                pageExtractor = new PageablePageParameterExtractor(index);
            } else if (Sort.class.isAssignableFrom(method.getParameterTypes()[index])) {
                if (sort != null) {
                    throw new QueryParserException(method.getDeclaringClass(), "You cannot specify both an order-by clause and a dynamic ordering");
                }
                pageExtractor = null;
            } else {
                throw new QueryParserException(method.getDeclaringClass(), "Invalid last argument: expected paging or sorting " + method);
            }
        } else {
            throw new QueryParserException(method.getDeclaringClass(), "Too many parameters declared for query method " + method);
        }
        return pageExtractor;
    }

    private int parseExpression(RepositoryMetadata repositoryMetadata, Method method, String methodName, boolean allIgnoreCase, DocumentReader reader, List<List<Parameter>> branches) {
        int index = 0;
        branches.add(new LinkedList<Parameter>());
        while (reader.hasMore()) {
            final Parameter parameter;
            //read a full expression
            String expression = parseInitialExpression(reader);
            //if the expression ended in Or, this is the end of this branch
            boolean branchEnd = expression.endsWith("Or");
            //if the expression contains an OrderBy, it is not only the end of the branch, but also the end of the query
            boolean expressionEnd = expression.matches(".+[a-z]OrderBy[A-Z].+");
            expression = handleExpressionEnd(reader, expression, expressionEnd);
            final Set<Modifier> modifiers = new HashSet<>();
            expression = parseModifiers(allIgnoreCase, expression, modifiers);
            //if the expression ends in And/Or, we expect there to be more
            if (expression.matches(".*?(And|Or)$") && !reader.hasMore()) {
                throw new QueryParserException(method.getDeclaringClass(), "Expected more tokens to follow AND/OR operator");
            }
            expression = expression.replaceFirst("(And|Or)$", "");
            String foundProperty = null;
            Operator operator = parseOperator(expression);
            if (operator != null) {
                foundProperty = expression.substring(0, expression.length() - ((MatchedOperator) operator).getMatchedToken().length());
            }
            //if no operator was found, it is the implied "IS" operator
            if (operator == null || foundProperty.isEmpty()) {
                foundProperty = expression;
                operator = operatorContext.getBySuffix(DEFAULT_OPERATOR_SUFFIX);
            }
            final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(repositoryMetadata, method, foundProperty);
            final String property = propertyDescriptor.getPath();
            //we need to match the method parameters with the operands for the designated operator
            final int[] indices = new int[operator.getOperands()];
            index = parseParameterIndices(method, methodName, index, operator, propertyDescriptor, indices);
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
        return index;
    }

    private com.mmnaseri.utils.spring.data.query.Sort parseSort(RepositoryMetadata repositoryMetadata, Method method, DocumentReader reader) {
        final com.mmnaseri.utils.spring.data.query.Sort sort;
        //let's figure out if there is a sort requirement embedded in the query definition
        if (reader.read("OrderBy") != null) {
            final List<Order> orders = new ArrayList<>();
            while (reader.hasMore()) {
                orders.add(parseOrder(method, reader, repositoryMetadata));
            }
            sort = new ImmutableSort(orders);
        } else {
            sort = null;
        }
        return sort;
    }

    private int parseParameterIndices(Method method, String methodName, int index, Operator operator, PropertyDescriptor propertyDescriptor, int[] indices) {
        int parameterIndex = index;
        for (int i = 0; i < operator.getOperands(); i++) {
            if (parameterIndex >= method.getParameterTypes().length) {
                throw new QueryParserException(method.getDeclaringClass(), "Expected to see parameter with index " + parameterIndex);
            }
            if (!propertyDescriptor.getType().isAssignableFrom(method.getParameterTypes()[parameterIndex])) {
                throw new QueryParserException(method.getDeclaringClass(), "Expected parameter " + parameterIndex + " on method " + methodName + " to be a descendant of " + propertyDescriptor.getType());
            }
            indices[i] = parameterIndex ++;
        }
        return parameterIndex;
    }

    private PropertyDescriptor getPropertyDescriptor(RepositoryMetadata repositoryMetadata, Method method, String property) {
        //let's get the property descriptor
        final PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(repositoryMetadata.getEntityType(), property);
        } catch (Exception e) {
            throw new QueryParserException(method.getDeclaringClass(), "Could not find property `" + StringUtils.uncapitalize(property) + "` on `" + repositoryMetadata.getEntityType() + "`", e);
        }
        return propertyDescriptor;
    }

    private Operator parseOperator(String expression) {
        Operator operator = null;
        //let's find out the operator that covers the longest suffix of the operation
        for (int i = 1; i < expression.length(); i++) {
            final String suffix = expression.substring(i);
            operator = operatorContext.getBySuffix(suffix);
            if (operator != null) {
                operator = new ImmutableMatchedOperator(operator, suffix);
                break;
            }
        }
        return operator;
    }

    private String parseModifiers(boolean allIgnoreCase, String originalExpression, Set<Modifier> modifiers) {
        String expression = originalExpression;
        if (expression.matches(".*" + IGNORE_CASE_SUFFIX)) {
            //if the expression ended in IgnoreCase, we need to strip that off
            modifiers.add(Modifier.IGNORE_CASE);
            expression = expression.replaceFirst(IGNORE_CASE_SUFFIX, "");
        } else if (allIgnoreCase) {
            //if we had already set "AllIgnoreCase", we will still add the modifier
            modifiers.add(Modifier.IGNORE_CASE);
        }
        return expression;
    }

    private String handleExpressionEnd(DocumentReader reader, String originalExpression, boolean expressionEnd) {
        String expression = originalExpression;
        if (expressionEnd) {
            //if that is the case, we need to put back the entirety of the order by clause
            int length = expression.length();
            expression = expression.replaceFirst("^(.+[a-z])OrderBy[A-Z].+$", "$1");
            length -= expression.length();
            reader.backtrack(length);
        }
        return expression;
    }

    private String parseInitialExpression(DocumentReader reader) {
        String expression = reader.expect("(.*?)(And[A-Z]|Or[A-Z]|$)");
        if (expression.matches(".*?(And|Or)[A-Z]")) {
            //if the expression ended in And/Or, we need to put the one extra character we scanned back
            //we scan one extra character because we don't want anything like "Order" to be mistaken for "Or"
            reader.backtrack(1);
            expression = expression.substring(0, expression.length() - 1);
        }
        return expression;
    }

    private Order parseOrder(Method method, DocumentReader reader, RepositoryMetadata repositoryMetadata) {
        String expression = reader.expect(".*?(Asc|Desc)");
        final SortDirection direction;
        if (expression.endsWith(ASC_SUFFIX)) {
            direction = SortDirection.ASCENDING;
            expression = expression.substring(0, expression.length() - ASC_SUFFIX.length());
        } else {
            direction = SortDirection.DESCENDING;
            expression = expression.substring(0, expression.length() - DESC_SUFFIX.length());
        }
        final PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(repositoryMetadata.getEntityType(), expression);
        } catch (Exception e) {
            throw new QueryParserException(method.getDeclaringClass(), "Failed to get a property descriptor for expression: " + expression, e);
        }
        if (!Comparable.class.isAssignableFrom(propertyDescriptor.getType())) {
            throw new QueryParserException(method.getDeclaringClass(), "Sort property `" + propertyDescriptor.getPath() + "` is not comparable in `" + method.getName() + "`");
        }
        return new ImmutableOrder(direction, propertyDescriptor.getPath(), NullHandling.DEFAULT);
    }

    private String parseFunctionName(Method method, DocumentReader reader) {
        //the first word in the method name is the function name
        String function = reader.read(Pattern.compile("^[a-z]+"));
        if (function == null) {
            throw new QueryParserException(method.getDeclaringClass(), "Malformed query method name: " + method);
        }
        //if the method name is one of the following, it is a simple read, and no function is required
        if (Arrays.asList("read", "find", "query", "get", "load", "select").contains(function)) {
            function = null;
        }
        return function;
    }

    private QueryModifiers parseQueryModifiers(Method method, DocumentReader reader) {
        //this is the limit set on the number of items being returned
        int limit = 0;
        //this is the flag that determines whether or not the result should be sifted for distinct values
        boolean distinct = false;
        //we are still reading the function name if we haven't gotten to `By` and we haven't seen
        //any of the magic keywords `First`, `Top`, and `Distinct`.
        //scan for words prior to 'By'
        while (reader.hasMore() && !reader.has("By")) {
            //if the next word is Top, then we are setting a limit
            if (reader.has("First")) {
                if (limit > 0) {
                    throw new QueryParserException(method.getDeclaringClass(), "There is already a limit of " + limit + " specified for this query: " + method);
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
                    throw new QueryParserException(method.getDeclaringClass(), "There is already a limit of " + limit + " specified for this query: " + method);
                }
                reader.expect("Top");
                limit = Integer.parseInt(reader.expect("\\d+"));
                continue;
            } else if (reader.has("Distinct")) {
                //if the next word is 'Distinct', we are saying we should return distinct results
                if (distinct) {
                    throw new QueryParserException(method.getDeclaringClass(), "You have already stated that this query should return distinct items: " + method);
                }
                distinct = true;
            }
            //we read the words until we reach "By".
            reader.expect("[A-Z][a-z]+");
        }
        return new QueryModifiers(limit, distinct);
    }

    public OperatorContext getOperatorContext() {
        return operatorContext;
    }

}
