package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

import java.util.Collection;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:36 PM)
 */
public interface ProxiedClass {

    void saySomething(String something, Double number);

    void saySomething(String something, float number);

    void saySomething(String something, Integer number);

    void saySomething(String something, Boolean flag);

    void doSomething();

    void somethingToAnObject(Iterable iterable);

    void somethingToAnObject(Collection iterable);

}
