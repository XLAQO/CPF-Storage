package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasIsValid<T extends HasIsValid<T>> {

  Boolean getIsValid();

  T withIsValid(Boolean isValid);

  static <U extends HasIsValid<U>, F extends HasIsValid<F>> UnaryOperator<U> addIsValid(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getIsValid)
        .map(to::withIsValid)
        .orElse(to);
  }

}
