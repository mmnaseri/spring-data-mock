package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:36 PM)
 */
public interface ProxiedClass {

    void saySomething(String something, Double number);

    void saySomething(String something, float number);

    void saySomething(String something, Integer number);

    void saySomething(String something, Boolean flag);

    void doSomething();

}
