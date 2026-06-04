package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.exceptions.InternalApplicationException;

public interface HasGraph<T extends HasGraph<T>> {

  String getGraph();

  default T withGraph(String graph) {
    throw new InternalApplicationException("withGraph is not supported for read-only type:" + this.getClass().getSimpleName());
  }

  static <U extends HasGraph<U>, F extends HasGraph<F>> UnaryOperator<U> addGraph(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getGraph)
        .map(to::withGraph)
        .orElse(to);
  }

  static <U extends HasGraph<U>, F extends HasGraph<F>> UnaryOperator<U> addGraph(Optional<F> maybeFrom) {
    return (U to) -> maybeFrom
        .map(F::getGraph)
        .map(to::withGraph)
        .orElse(to);
  }

  static <U extends HasGraph<U>, F> UnaryOperator<U> addGraphIfPresent(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasGraph<?> has)
            ? Optional.of(has)
            : Optional.empty())
        .map(HasGraph::getGraph)
        .map(to::withGraph)
        .orElse(to);
  }
}
