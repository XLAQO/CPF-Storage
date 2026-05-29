package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasIsDefault<T extends HasIsDefault<T>> {

  Boolean getIsDefault();

  T withIsDefault(Boolean isDefault);

  static <U extends HasIsDefault<U>, F extends HasIsDefault<F>> UnaryOperator<U> addIsDefault(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getIsDefault)
        .map(to::withIsDefault)
        .orElse(to);
  }

}
