package com.mmnaseri.utils.spring.data.sample.usecases.store;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:19 PM)
 */
public class SampleAuditorAware implements AuditorAware<String> {

    public static final String AUDITOR = "AUDITOR";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(AUDITOR);
    }

}
