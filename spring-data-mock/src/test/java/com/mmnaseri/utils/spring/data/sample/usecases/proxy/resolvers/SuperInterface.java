package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:35 PM)
 */
public interface SuperInterface {

    void somethingToAnObject(Object object);

    void somethingToAnObject(Iterable iterable);


    void saySomething(CharSequence sequence, Double number);

    void doSomething();


}
