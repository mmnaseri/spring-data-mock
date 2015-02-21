package com.agileapes.utils.spring.domain.matchers;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:55)
 */
public class LessThanMatcher extends AbstractComparingMatcher {

    @Override
    public int compareFirstToSecond() {
        return -1;
    }

}
