package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import java.util.concurrent.Callable;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:55 PM)
 */
public class ErrorThrowingCallable implements Callable {

    @Override
    public Object call() throws Exception {
        throw new RuntimeException();
    }
}
