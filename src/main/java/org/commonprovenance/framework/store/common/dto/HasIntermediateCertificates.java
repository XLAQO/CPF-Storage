package org.commonprovenance.framework.store.common.dto;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.exceptions.InternalApplicationException;

public interface HasIntermediateCertificates<T extends HasIntermediateCertificates<T>> {

  List<String> getIntermediateCertificates();

  default T withIntermediateCertificates(List<String> intermediateCertificates) {
    throw new InternalApplicationException("withIntermediateCertificates is not supported for read-only type:" + this.getClass().getSimpleName());
  }

  static <U extends HasIntermediateCertificates<U>, F extends HasIntermediateCertificates<F>> UnaryOperator<U> addIntermediateCertificates(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getIntermediateCertificates)
        .map(to::withIntermediateCertificates)
        .orElse(to);
  }

  static <U extends HasIntermediateCertificates<U>, F> UnaryOperator<U> addIntermediateCertificatesIfPresent(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasIntermediateCertificates<?> has)
            ? Optional.of(has)
            : Optional.empty())
        .map(HasIntermediateCertificates::getIntermediateCertificates)
        .map(to::withIntermediateCertificates)
        .orElse(to);
  }

}
