package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.exceptions.InternalApplicationException;

public interface HasClientCertificate<T extends HasClientCertificate<T>> {

  String getClientCertificate();

  default T withClientCertificate(String clientCertificate) {
    throw new InternalApplicationException("withClientCertificate is not supported for read-only type:" + this.getClass().getSimpleName());
  }

  static <U extends HasClientCertificate<U>, F extends HasClientCertificate<F>> UnaryOperator<U> addClientCertificate(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getClientCertificate)
        .map(to::withClientCertificate)
        .orElse(to);
  }

  static <U extends HasClientCertificate<U>, F> UnaryOperator<U> addClientCertificateIfPresent(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap((F v) -> (v instanceof HasClientCertificate<?> has)
            ? Optional.of(has)
            : Optional.empty())
        .map(HasClientCertificate::getClientCertificate)
        .map(to::withClientCertificate)
        .orElse(to);
  }

}
