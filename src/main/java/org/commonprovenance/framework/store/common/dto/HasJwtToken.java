package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasJwtToken<T extends HasJwtToken<T>> {
  String getJwt();

  T withJwt(String jwtToken);

  static <T extends HasJwtToken<T>, F extends HasJwtToken<F>> UnaryOperator<T> addJwt(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getJwt)
        .map(to::withJwt)
        .orElse(to);
  }

}
