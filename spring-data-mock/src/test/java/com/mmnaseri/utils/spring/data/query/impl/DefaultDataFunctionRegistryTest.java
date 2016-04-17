package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.DuplicateFunctionException;
import com.mmnaseri.utils.spring.data.error.FunctionNotFoundException;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class DefaultDataFunctionRegistryTest {

    @Test
    public void testDefaultFunctions() throws Exception {
        final DefaultDataFunctionRegistry registry = new DefaultDataFunctionRegistry();
        final Set<String> functions = registry.getFunctions();
        final Set<String> expected = new HashSet<>(Arrays.asList("count", "delete"));
        assertThat(functions, hasSize(2));
        for (String function : functions) {
            assertThat(function, isIn(expected));
            expected.remove(function);
        }
        assertThat(expected, is(Matchers.<String>empty()));
    }

    @Test(expectedExceptions = FunctionNotFoundException.class)
    public void testNonExistentFunction() throws Exception {
        final DefaultDataFunctionRegistry registry = new DefaultDataFunctionRegistry();
        registry.getFunction("xyz");
    }

    @Test(expectedExceptions = DuplicateFunctionException.class)
    public void testRegisteringDuplicateFunction() throws Exception {
        final DefaultDataFunctionRegistry registry = new DefaultDataFunctionRegistry();
        registry.register("count", new DeleteDataFunction());
    }

    @Test
    public void testRegisteringLegitimateFunction() throws Exception {
        final DefaultDataFunctionRegistry registry = new DefaultDataFunctionRegistry();
        final String item = "size";
        final DataFunction function = new CountDataFunction();
        registry.register(item, function);
        assertThat(registry.getFunctions(), hasItem(item));
        assertThat(registry.getFunction(item), is(function));
    }

}