package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.model.Format;

public interface HasFormat<T extends HasFormat<T>> {
  Format getFormat();

  T withFormat(Format format);

  static <T extends HasFormat<T>, F extends HasFormat<F>> UnaryOperator<T> addFormat(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getFormat)
        .map(to::withFormat)
        .orElse(to);
  }

}
