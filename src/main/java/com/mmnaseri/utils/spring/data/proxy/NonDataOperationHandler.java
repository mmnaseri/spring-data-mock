package com.mmnaseri.utils.spring.data.proxy;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public interface NonDataOperationHandler {

    boolean handles(Object proxy, Method method, Object... args);

    Object invoke(Object proxy, Object... args);

}
