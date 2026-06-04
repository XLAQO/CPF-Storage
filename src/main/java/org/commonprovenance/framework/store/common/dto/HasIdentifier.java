package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.exceptions.InternalApplicationException;

public interface HasIdentifier<T extends HasIdentifier<T>> {
  String getIdentifier();

  default T withIdentifier(String identifier) {
    throw new InternalApplicationException("withIdentifier is not supported for read-only type:" + this.getClass().getSimpleName());
  }

  static <T extends HasIdentifier<T>, F extends HasIdentifier<F>> UnaryOperator<T> addIdentifier(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getIdentifier)
        .map(to::withIdentifier)
        .orElse(to);
  }

  static <T extends HasIdentifier<T>, F extends HasIdentifierOptional<F>> UnaryOperator<T> addIdentifier(F from) {
    return (T to) -> Optional.ofNullable(from)
        .flatMap(F::getIdentifier)
        .map(to::withIdentifier)
        .orElse(to);
  }

  static <T extends HasIdentifier<T>, F> UnaryOperator<T> addIdentifierIfPresent(F from) {
    return (T to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasIdentifier<?> has)
            ? Optional.of(has).map(HasIdentifier::getIdentifier)
            : (v instanceof HasIdentifierOptional<?> maybeHas)
                ? maybeHas.getIdentifier()
                : Optional.empty())
        .map(to::withIdentifier)
        .orElse(to);
  }

}
