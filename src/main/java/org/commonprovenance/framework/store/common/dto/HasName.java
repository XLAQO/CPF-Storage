package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasName<T extends HasName<T>> {

  String getName();

  T withName(String name);

  static <U extends HasName<U>, F extends HasName<F>> UnaryOperator<U> addName(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getName)
        .map(to::withName)
        .orElse(to);
  }
}
