package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.UUID;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class UUIDKeyGenerator implements KeyGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
