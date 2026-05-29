package org.commonprovenance.framework.store.common.dto;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.model.Document;

public interface HasDocumentOptional<T extends HasDocumentOptional<T>> {
  Optional<Document> getDocument();

  T withDocument(Document document);

  static <T extends HasDocumentOptional<T>, F extends HasDocumentOptional<F>> UnaryOperator<T> addTrustedParty(F from) {
    return (T to) -> Optional.ofNullable(from)
        .flatMap(F::getDocument)
        .map(to::withDocument)
        .orElse(to);
  }

  static <T extends HasDocumentOptional<T>, F extends HasDocument<F>> UnaryOperator<T> addTrustedParty(F from) {
    return (T to) -> Optional.ofNullable(from)
        .map(F::getDocument)
        .map(to::withDocument)
        .orElse(to);
  }

}
