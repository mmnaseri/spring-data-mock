package com.mmnaseri.utils.spring.data.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:51 PM)
 */
public class TestUtils {

    public static  <E> List<E> iterableToList(Iterable<E> iterable) {
        final List<E> list = new ArrayList<>();
        for (E item : iterable) {
            list.add(item);
        }
        return list;
    }


}
