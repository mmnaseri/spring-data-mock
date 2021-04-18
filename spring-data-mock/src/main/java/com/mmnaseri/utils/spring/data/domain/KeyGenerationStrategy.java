package com.mmnaseri.utils.spring.data.domain;

public enum KeyGenerationStrategy {
    /**
     * Generate a key for {@code null} id values only.
     */
    ONLY_NULL,

    /**
     * Generate a key for all "unmanaged" entites.
     * Regardless of whether an ID value exists or not.
     */
    ALL_UNMANAGED
}
