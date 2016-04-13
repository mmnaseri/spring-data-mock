package com.mmnaseri.utils.spring.data.sample.usecases.store;

import org.springframework.data.domain.AuditorAware;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:19 PM)
 */
public class SampleAuditorAware implements AuditorAware<String> {

    public static final String AUDITOR = "AUDITOR";

    @Override
    public String getCurrentAuditor() {
        return AUDITOR;
    }

}
