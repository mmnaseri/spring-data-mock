package com.agileapes.utils.spring;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/21 AD, 16:14)
 */
public interface DataOperation<E, K extends Serializable> {

    Object execute(Map<K, E> dataSet, Object... parameters);

}
