package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasIsChecked<T extends HasIsChecked<T>> {

  Boolean getIsChecked();

  T withIsChecked(Boolean isChecked);

  static <U extends HasIsChecked<U>, F extends HasIsChecked<F>> UnaryOperator<U> addIsChecked(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getIsChecked)
        .map(to::withIsChecked)
        .orElse(to);
  }

  static <U extends HasIsChecked<U>, F> UnaryOperator<U> addIsCheckedIfPresent(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasIsChecked<?> has)
            ? Optional.of(has)
            : Optional.empty())
        .map(HasIsChecked::getIsChecked)
        .map(to::withIsChecked)
        .orElse(to);
  }

}
