package com.agileapes.utils.spring.domain;

import com.agileapes.utils.spring.RepositoryDescriptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.util.StringUtils;

import java.awt.print.Pageable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 21:47)
 */
public class QueryMethodMetadata<E, K extends Serializable, R extends Repository<E, K>> {

    private boolean array;
    private boolean paging = false;
    private boolean sorting = false;
    private boolean read = false;
    private boolean distinct = false;
    private boolean collection = false;
    private boolean allIgnoreCase = false;
    private Class<?> returnType = Object.class;
    private Class<?> collectionType = null;
    private String function;
    private int limit = 0;
    private Sort sort = null;
    private List<List<Parameter>> branches;

    public QueryMethodMetadata(Method method, RepositoryDescriptor<E, K, R> descriptor) {
        read = method.getName().matches("^(read|get|query|find).*");
        //we will first analyze the method's return type
        if (method.getReturnType().isAssignableFrom(descriptor.getEntityType())) {
            returnType = method.getReturnType();
        } else if (method.getReturnType().isArray() && method.getReturnType().getComponentType().isAssignableFrom(descriptor.getEntityType())) {
            array = true;
            returnType = method.getReturnType().getComponentType();
        } else if (method.getGenericReturnType() instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
            if (parameterizedType.getActualTypeArguments().length > 1) {
                throw new IllegalArgumentException("Parameterized type must have at most one argument: " + method.getName());
            } else if (parameterizedType.getActualTypeArguments().length == 0) {
                returnType = Object.class;
            } else if (parameterizedType.getActualTypeArguments()[0] instanceof Class<?> && ((Class<?>) parameterizedType.getActualTypeArguments()[0]).isAssignableFrom(descriptor.getEntityType())) {
                returnType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            } else {
                throw new IllegalArgumentException("Invalid type parameter: " + parameterizedType.getActualTypeArguments()[0]);
            }
            if (Iterable.class.isAssignableFrom(method.getReturnType())) {
                collection = true;
                collectionType = method.getReturnType();
            } else if (Page.class.isAssignableFrom(method.getReturnType())) {
                paging = true;
            }
        } else {
            if (read) {
                throw new IllegalArgumentException("Invalid query return type: " + method.getName());
            } else {
                returnType = method.getReturnType();
            }
        }
        //now, let's find out about the method's name
        String expression = method.getName();
        final java.util.regex.Matcher functionMatcher = Pattern.compile("^(count|sum|average|delete|save|update|delete)").matcher(expression);
        if (functionMatcher.find()) {
            function = functionMatcher.group();
        }
        //let's strip the first word
        expression = expression.replaceAll("^[a-z]+", "");
        //now we will skim for query modifiers
        while (!expression.startsWith("By")) {
            //let's determine if the query is distinct
            if (expression.startsWith("Distinct")) {
                distinct = true;
                expression = expression.substring("Distinct".length());
            } else if (expression.startsWith("First") || expression.startsWith("Top")) {
                //this will tell us if we are limiting the results
                expression = expression.replaceAll("^(First|Top)", "");
                final java.util.regex.Matcher matcher = Pattern.compile("^\\d+").matcher(expression);
                if (matcher.find()) {
                    limit = Integer.parseInt(matcher.group());
                    expression = expression.substring(matcher.group().length());
                } else {
                    limit = 1;
                }
            } else if (expression.matches("^[A-Z][a-z]*.*")) {
                //we skip any unknown words
                expression = expression.replaceFirst("^[A-Z][a-z]*", "");
            } else {
                throw new IllegalArgumentException("Invalid expression '" + expression + "' in method name: " + method.getName());
            }
        }
        //let's jump over "By"
        expression = expression.substring("By".length());
        final List<String> tokens = new ArrayList<String>();
        while (!expression.startsWith("OrderBy") && !expression.startsWith("AllIgnoreCase") && !expression.isEmpty()) {
            //we find out a property name (+ operators and modifiers) when we reach the ordering or other query modifiers or the end of the expression
            String property = "";
            while (!expression.matches("^(And|Or)[A-Z].*") && !expression.startsWith("OrderBy") && !expression.startsWith("AllIgnoreCase") && !expression.isEmpty()) {
                property += expression.substring(0, 1);
                expression = expression.substring(1);
            }
            tokens.add(property);
            //we will insert tokens for conjunction operators
            if (expression.matches("^And[A-Z].*")) {
                tokens.add("&&");
                expression = expression.substring("And".length());
                if (expression.startsWith("OrderBy") || expression.startsWith("AllIgnoreCase") || expression.isEmpty()) {
                    throw new IllegalArgumentException("Expected a property selector after AND: " + method.getName());
                }
            } else if (expression.matches("^Or[A-Z].*")) {
                tokens.add("||");
                expression = expression.substring("Or".length());
                if (expression.startsWith("OrderBy") || expression.startsWith("AllIgnoreCase") || expression.isEmpty()) {
                    throw new IllegalArgumentException("Expected a property selector after OR: " + method.getName());
                }
            }
        }
        //since we manually insert one conjunction per operand, there should be an odd number of tokens
        if (tokens.size() % 2 != 1) {
            throw new IllegalArgumentException("Something is odd, since tokens are not odd! ;-)");
        }
        //this is to even the token numbers out
        tokens.add("$");
        //if the ignore case is applied to all parameters we should take that into account
        if (!expression.isEmpty() && expression.startsWith("AllIgnoreCase")) {
            allIgnoreCase = true;
            expression = expression.substring("AllIgnoreCase".length());
        } else {
            allIgnoreCase = false;
        }
        //if there is something left at this point, it has to be the ordering criteria
        if (!expression.isEmpty()) {
            final ArrayList<Sort.Order> orders = new ArrayList<Sort.Order>();
            expression = expression.substring("OrderBy".length());
            if (expression.isEmpty()) {
                throw new IllegalArgumentException("Expected to see an ordering criteria: " + method.getName());
            }
            while (!expression.isEmpty()) {
                String property = "";
                //we assume orderings to be separated by ANDs
                while (!expression.matches("^And[A-Z].*") && !expression.isEmpty()) {
                    property += expression.substring(0, 1);
                    expression = expression.substring(1);
                }
                //if we don't explicitly demand a descending ordering, it will be ascending
                if (property.endsWith("Desc")) {
                    property = property.substring(0, property.length() - "Desc".length());
                    orders.add(new Sort.Order(Sort.Direction.DESC, StringUtils.uncapitalize(property)));
                } else {
                    property = property.replaceAll("Asc$", "");
                    orders.add(new Sort.Order(Sort.Direction.ASC, StringUtils.uncapitalize(property)));
                }
                if (expression.startsWith("And")) {
                    expression = expression.substring("And".length());
                    if (expression.isEmpty()) {
                        throw new IllegalArgumentException("Expected to see more after AND in sorting criteria: " + method.getName());
                    }
                }
            }
            sort = new Sort(orders);
            if (!expression.isEmpty()) {
                throw new IllegalArgumentException("Invalid query tail '" + expression + "' in " + method.getName());
            }
        }
        //at this point, we will extract and match parameter information
        final List<List<String>> criteria = new ArrayList<List<String>>();
        criteria.add(new ArrayList<String>());
        //we will first make list whose each element is a list of those tokens that are conjuncted by ANDs
        //this allows for preserving priority between AND and OR
        for (int i = 0; i < tokens.size(); i += 2) {
            criteria.get(criteria.size() - 1).add(tokens.get(i));
            if (tokens.get(i + 1).equals("||")) {
                criteria.add(new ArrayList<String>());
            }
        }
        //let's create a list of operator suffixes ordered descending based on their length, so that the longest is
        //at the front
        final List<String> suffixes = new ArrayList<String>();
        for (Operator operator : Operator.values()) {
            suffixes.addAll(Arrays.asList(operator.getSuffix()));
        }
        Collections.sort(suffixes, new Comparator<String>() {
            @Override
            public int compare(String first, String second) {
                return Integer.compare(second.length(), first.length());
            }
        });
        //each branch will be a set of rules upon the successful matching of all of which that branch, and therefore the
        //whole matching is considered to be done
        branches = new ArrayList<List<Parameter>>(criteria.size());
        int index = 0;
        for (List<String> current : criteria) {
            branches.add(new ArrayList<Parameter>());
            for (String token : current) {
                boolean ignoreCase = false;
                if (token.endsWith("IgnoreCase")) {
                    ignoreCase = true;
                    token = token.substring(0, token.length() - "IgnoreCase".length());
                }
                ignoreCase = ignoreCase || allIgnoreCase;
                Operator operator = null;
                for (String suffix : suffixes) {
                    if (token.endsWith(suffix)) {
                        operator = Operator.getBySuffix(suffix);
                        token = token.substring(0, token.length() - suffix.length());
                        break;
                    }
                }
                if (operator == null) {
                    operator = Operator.EQUAL_TO;
                }
                final int[] indices = new int[operator.getOperands()];
                for (int i = 0; i < indices.length; i ++) {
                    indices[i] = index ++;
                }
                branches.get(branches.size() - 1).add(new Parameter(StringUtils.uncapitalize(token), operator, indices, ignoreCase));
            }
        }
        if (method.getParameterTypes().length < index) {
            //we should have at least the same number of parameters as required by the operators
            throw new IllegalArgumentException("There are less parameters for the query method than expressed in the query name: " + method.getName());
        } else if (method.getParameterTypes().length == index + 1) {
            //if we have one additional parameter, it has to be either for paging or for sorting
            if (Pageable.class.isAssignableFrom(method.getParameterTypes()[index])) {
                paging = true;
            } else if (Sort.class.isAssignableFrom(method.getParameterTypes()[index])) {
                if (paging) {
                    throw new IllegalArgumentException("When the return type of the method is a Page, you should have an extra parameter for paging specifications: " + method.getName());
                }
                if (sort != null) {
                    throw new IllegalArgumentException("You cannot pass a dynamic sort option to a method which specifies static sorting: " + method.getName());
                }
                sorting = true;
            } else {
                throw new IllegalArgumentException("Position " + (index + 1) + " is reserved for the special argument which should be either a Pageable or a Sort: " + method.getName());
            }
        } else if (method.getParameterTypes().length != index) {
            throw new IllegalArgumentException("There are more parameters specified for the method than should have been: " + method.getName());
        } else if (paging) {
            //if the number of parameters adds up, but we are expecting a paging specification parameter, we should raise an error
            throw new IllegalArgumentException("When the return type of the method is a Page, you should have an extra parameter for paging specifications: " + method.getName());
        }
        //now let's check and see if parameter types for the method match with those of the named properties for the query
        final BeanWrapper wrapper = new BeanWrapperImpl(descriptor.getEntityType());
        for (List<Parameter> branch : branches) {
            for (Parameter parameter : branch) {
                if (parameter.getIndices().length == 0 ) {
                    continue;
                }
                for (int i = parameter.getIndices()[0]; i <= parameter.getIndices()[parameter.getIndices().length - 1]; i ++) {
                    final String parameterName = parameter.getName();
                    final Class<?> type = wrapper.getPropertyType(parameterName);
                    if (!type.isAssignableFrom(method.getParameterTypes()[i])) {
                        if (parameter.getOperator().isCollection() && Iterable.class.isAssignableFrom(method.getParameterTypes()[i])) {
                            final Type genericType = method.getGenericParameterTypes()[i];
                            final Class<?> componentType;
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                if (parameterizedType.getActualTypeArguments().length == 1 && parameterizedType.getActualTypeArguments()[0] instanceof Class<?>) {
                                    componentType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                                } else {
                                    throw new IllegalArgumentException("Expected parameter " + i + " to specify " + type + " as its component in " + method.getName());
                                }
                            } else {
                                componentType = Object.class;
                            }
                            if (!type.isAssignableFrom(componentType)) {
                                throw new IllegalArgumentException("Expected parameter " + i + " to specify " + type + " as its component in " + method.getName());
                            }
                        } else {
                            throw new IllegalArgumentException("Expected parameter " + i + " corresponding to property " + parameterName + " to be of type " + type + ": " + method.getName());
                        }
                    }
                }
            }
        }
    }

    public boolean isPaging() {
        return paging;
    }

    public boolean isSorting() {
        return sorting;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public boolean isCollection() {
        return collection;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Class<?> getCollectionType() {
        return collectionType;
    }

    public int getLimit() {
        return limit;
    }

    public Sort getSort() {
        return sort;
    }

    public List<List<Parameter>> getBranches() {
        return branches;
    }

    public boolean isAllIgnoreCase() {
        return allIgnoreCase;
    }

    public boolean isArray() {
        return array;
    }

    public String getFunction() {
        return function;
    }
}
