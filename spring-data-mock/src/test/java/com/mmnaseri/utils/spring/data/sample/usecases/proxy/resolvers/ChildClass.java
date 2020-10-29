package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:36 PM)
 */
@SuppressWarnings({"unused", "EmptyMethod"})
public class ChildClass extends SuperClass {

    public void saySomething(String string, Integer number) {
        //this nativeMethod's signature will match the name plus parameter types (? extends String, ? extends Integer)
        //since both of these classes are final, it means that it will only match the exact same nativeMethod
    }

    public void doSomething() {
        //this nativeMethod's signature is empty, so only its name will be considered
    }

}
