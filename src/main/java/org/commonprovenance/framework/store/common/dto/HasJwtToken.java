package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.common.utils.JwtUtils;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.InternalApplicationException;

import io.vavr.control.Either;

public interface HasJwtToken<T extends HasJwtToken<T>> {
  String getJwt();

  default T withJwt(String jwtToken) {
    throw new InternalApplicationException("withJwt is not supported for read-only type:" + this.getClass().getSimpleName());
  }

  static <T extends HasJwtToken<T>, F extends HasJwtToken<F>> UnaryOperator<T> addJwt(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getJwt)
        .map(to::withJwt)
        .orElse(to);
  }

  static <T extends HasJwtToken<T> & HasCreatedOn<T>> Either<ApplicationException, T> loadCreatedOn(T value) {
    return Either.<ApplicationException, T> right(value)
        .map(T::getJwt)
        .flatMap(JwtUtils::extractTokenTimestamp)
        .map(value::withCreatedOn);
  }
}
