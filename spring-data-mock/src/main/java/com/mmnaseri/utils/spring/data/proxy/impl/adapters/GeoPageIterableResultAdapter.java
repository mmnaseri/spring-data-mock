package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will adapt results from an iterable object to a geo page.
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the
 * required return type is an instance of {@link GeoPage}. Remember that it does <em>not</em> check
 * for individual object type compatibility.
 *
 * <p>This adapter will execute at priority {@literal -150}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class GeoPageIterableResultAdapter extends AbstractIterableResultAdapter<GeoPage> {

  public GeoPageIterableResultAdapter() {
    super(-150);
  }

  @Override
  protected GeoPage doAdapt(Invocation invocation, Iterable iterable) {
    final List content = new ArrayList();
    for (Object item : iterable) {
      //noinspection unchecked
      content.add(item);
    }
    //noinspection unchecked
    return new GeoPage(new GeoResults(content));
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return originalValue instanceof Iterable
        && invocation.getMethod().getReturnType().equals(GeoPage.class);
  }
}
