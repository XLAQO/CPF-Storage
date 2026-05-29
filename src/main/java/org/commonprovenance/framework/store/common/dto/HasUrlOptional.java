package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface HasUrlOptional<T extends HasUrlOptional<T>> {

  Optional<String> getUrl();

  T withUrl(String url);

  static <U extends HasUrlOptional<U>, F extends HasUrlOptional<F>> UnaryOperator<U> addUrl(F from) {
    return (U to) -> Optional.ofNullable(from)
        .flatMap(F::getUrl)
        .map(to::withUrl)
        .orElse(to);
  }

  static <U extends HasUrlOptional<U>, F extends HasUrl<F>> UnaryOperator<U> addUrl(F from) {
    return (U to) -> Optional.ofNullable(from)
        .map(F::getUrl)
        .map(to::withUrl)
        .orElse(to);
  }
}
