package org.commonprovenance.framework.store.controller.facade;

import org.commonprovenance.framework.store.controller.dto.form.OrganizationFormDTO;
import org.commonprovenance.framework.store.controller.dto.response.OrganizationResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;

import reactor.core.publisher.Mono;

public interface OrganizationFacade {
  Mono<OrganizationResponseDTO> register(OrganizationFormDTO body);

  Mono<OrganizationResponseDTO> update(OrganizationFormDTO body);

  Mono<OrganizationResponseDTO> getOrganizationByIdentifier(@PathVariable String identifier);
}
