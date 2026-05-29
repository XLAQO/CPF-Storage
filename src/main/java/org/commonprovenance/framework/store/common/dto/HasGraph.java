package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasGraph<T extends HasGraph<T>> {

  String getGraph();

  T withGraph(String graph);

  static <U extends HasGraph<U>, F extends HasGraph<F>> UnaryOperator<U> addGraph(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getGraph)
        .map(to::withGraph)
        .orElse(to);
  }
}
