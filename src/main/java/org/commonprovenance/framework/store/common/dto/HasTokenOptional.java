package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.model.Token;

public interface HasTokenOptional<T extends HasTokenOptional<T>> {
  Optional<Token> getToken();

  T withToken(Token token);

  static <T extends HasTokenOptional<T>, F extends HasTokenOptional<F>> UnaryOperator<T> addToken(F from) {
    return (T to) -> Optional.ofNullable(from)
        .flatMap(F::getToken)
        .map(to::withToken)
        .orElse(to);
  }

}
