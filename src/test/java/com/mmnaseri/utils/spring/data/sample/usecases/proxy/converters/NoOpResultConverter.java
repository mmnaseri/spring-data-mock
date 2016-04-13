package com.mmnaseri.utils.spring.data.sample.usecases.proxy.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.impl.converters.AbstractResultConverter;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:26 PM)
 */
public class NoOpResultConverter extends AbstractResultConverter {

    private Object original;

    @Override
    protected Object doConvert(Invocation invocation, Object original) {
        this.original = original;
        return original;
    }

    public Object getOriginal() {
        return original;
    }

}
