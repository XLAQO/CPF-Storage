package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasClientCertificate<T extends HasClientCertificate<T>> {

  String getClientCertificate();

  T withClientCertificate(String clientCertificate);

  static <U extends HasClientCertificate<U>, F extends HasClientCertificate<F>> UnaryOperator<U> addClientCertificate(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getClientCertificate)
        .map(to::withClientCertificate)
        .orElse(to);
  }
}
