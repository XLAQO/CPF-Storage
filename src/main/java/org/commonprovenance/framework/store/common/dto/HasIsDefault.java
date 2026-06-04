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

  static <U extends HasIsDefault<U>, F> UnaryOperator<U> addIsDefaultIfPresent(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasIsDefault<?> has)
            ? Optional.of(has)
            : Optional.empty())
        .map(HasIsDefault::getIsDefault)
        .map(to::withIsDefault)
        .orElse(to);
  }

}
