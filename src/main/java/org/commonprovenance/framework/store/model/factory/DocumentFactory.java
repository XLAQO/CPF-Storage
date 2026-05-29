package org.commonprovenance.framework.store.model.factory;

import org.commonprovenance.framework.store.controller.dto.form.DocumentFormDTO;
import org.commonprovenance.framework.store.model.Document;

public class DocumentFactory {
  public static Document fromFormDTO(DocumentFormDTO formDTO) {
    return new Document(
        formDTO.getDocument(),
        formDTO.getDocumentFormat(),
        formDTO.getSignature());
  }

}
