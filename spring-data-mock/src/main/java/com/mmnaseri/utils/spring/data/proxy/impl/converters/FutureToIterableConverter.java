package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultConversionFailureException;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

import java.util.concurrent.Future;

/**
 * This converter will convert a value that is of type {@link Future} to an iterable. Furthermore,
 * it will convert that value one more level to get to the final value, if required. Also, the
 * conversion will be a blocking statement that waits for the future value to be resolved via {@link
 * Future#get()}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
@SuppressWarnings("WeakerAccess")
public class FutureToIterableConverter extends AbstractResultConverter {

  @Override
  protected Object doConvert(Invocation invocation, Object original) {
    if (original instanceof Future) {
      Future future = (Future) original;
      final ResultConverter converter = new DefaultResultConverter();
      try {
        final Object result = future.get();
        return converter.convert(invocation, result);
      } catch (Exception e) {
        throw new ResultConversionFailureException(e);
      }
    }
    return original;
  }
}
