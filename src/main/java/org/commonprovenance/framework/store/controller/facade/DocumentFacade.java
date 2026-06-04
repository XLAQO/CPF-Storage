package org.commonprovenance.framework.store.controller.facade;

import org.commonprovenance.framework.store.controller.dto.form.DocumentFormDTO;
import org.commonprovenance.framework.store.controller.dto.response.TokenResponseDTO;

import reactor.core.publisher.Mono;

public interface DocumentFacade {
  Mono<TokenResponseDTO> createProvDocument(String organizationIdentifier, DocumentFormDTO body);

  Mono<Void> exists(String identifier);
}
