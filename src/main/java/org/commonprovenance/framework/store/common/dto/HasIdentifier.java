package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasIdentifier<T extends HasIdentifier<T>> {
  String getIdentifier();

  T withIdentifier(String identifier);

  static <T extends HasIdentifier<T>, F extends HasIdentifier<F>> UnaryOperator<T> addIdentifier(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getIdentifier)
        .map(to::withIdentifier)
        .orElse(to);
  }

  static <T extends HasIdentifier<T>, F extends HasIdentifierOptional> UnaryOperator<T> addIdentifier(F from) {
    return (T to) -> Optional.ofNullable(from)
        .flatMap(F::getIdentifier)
        .map(to::withIdentifier)
        .orElse(to);
  }
}
