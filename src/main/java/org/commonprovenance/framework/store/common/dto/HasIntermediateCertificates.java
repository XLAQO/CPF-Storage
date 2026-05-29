package org.commonprovenance.framework.store.common.dto;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasIntermediateCertificates<T extends HasIntermediateCertificates<T>> {

  List<String> getIntermediateCertificates();

  T withIntermediateCertificates(List<String> intermediateCertificates);

  static <U extends HasIntermediateCertificates<U>, F extends HasIntermediateCertificates<F>> UnaryOperator<U> addIntermediateCertificates(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getIntermediateCertificates)
        .map(to::withIntermediateCertificates)
        .orElse(to);
  }
}
