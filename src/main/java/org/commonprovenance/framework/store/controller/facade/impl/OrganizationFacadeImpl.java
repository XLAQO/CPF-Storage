package org.commonprovenance.framework.store.controller.facade.impl;

import static org.commonprovenance.framework.store.common.publisher.PublisherHelper.MONO;

import org.commonprovenance.framework.store.controller.dto.form.OrganizationFormDTO;
import org.commonprovenance.framework.store.controller.dto.response.OrganizationResponseDTO;
import org.commonprovenance.framework.store.controller.dto.response.factory.DTOFactory;
import org.commonprovenance.framework.store.controller.dto.response.factory.OrganizationResponseFactory;
import org.commonprovenance.framework.store.controller.facade.OrganizationFacade;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.BadRequestException;
import org.commonprovenance.framework.store.exceptions.ConflictException;
import org.commonprovenance.framework.store.exceptions.InternalApplicationException;
import org.commonprovenance.framework.store.model.TrustedParty;
import org.commonprovenance.framework.store.model.factory.OrganizationFactory;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.OrganizationService;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.TrustedPartyService;
import org.commonprovenance.framework.store.service.web.trustedParty.TrustedPartyWebService;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class OrganizationFacadeImpl implements OrganizationFacade {
  private final OrganizationService organizationService;
  private final TrustedPartyService trustedPartyService;

  private final TrustedPartyWebService trustedPartyWebService;

  public OrganizationFacadeImpl(
      OrganizationService organizationService,
      TrustedPartyService trustedPartyService,
      TrustedPartyWebService trustedPartyWebService) {
    this.organizationService = organizationService;
    this.trustedPartyService = trustedPartyService;

    this.trustedPartyWebService = trustedPartyWebService;
  }

  @Override
  public Mono<OrganizationResponseDTO> register(OrganizationFormDTO body) {
    return Mono.just(body)
        .delayUntil(MONO.makeSureNotNull(new BadRequestException("Request body can not be null or empty!")))
        .map(OrganizationFactory::fromFormDTO)
        .flatMap(this.trustedPartyWebService.setTrustedPartyByBaseUrl(body.getTrustedPartyUri()))
        .delayUntil(MONO.makeSureAsync(
            this.trustedPartyService::isTrustedPartyValid,
            organization -> organization.getTrustedParty().flatMap(TrustedParty::getUrlIfNotDefault)
                .<ApplicationException> map(baseUrl -> new ConflictException("TrustedParty at '" + baseUrl + "' is not registered in CPF-Store!"))
                .orElse(new InternalApplicationException("Default TrustedParty is not registered in CPF-Store!"))))
        // TODO: Rollback if Organization registration fail on NRO side.
        .delayUntil(this.organizationService::storeOrganization)
        .delayUntil(this.trustedPartyWebService::registerOrganization)
        .map(OrganizationResponseFactory::fromModel);
  }

  @Override
  public Mono<OrganizationResponseDTO> update(OrganizationFormDTO body) {
    return Mono.just(body)
        .delayUntil(MONO.makeSureNotNull(new BadRequestException("Request body can not be null or empty!")))
        .map(OrganizationFactory::fromFormDTO)
        .flatMap(this.trustedPartyWebService.setTrustedPartyByBaseUrl(body.getTrustedPartyUri()))
        .delayUntil(MONO.makeSureAsync(
            this.trustedPartyService::isTrustedPartyValid,
            organization -> organization.getTrustedParty().flatMap(TrustedParty::getUrlIfNotDefault)
                .<ApplicationException> map(baseUrl -> new ConflictException("TrustedParty at '" + baseUrl + "' is not registered in CPF-Store!"))
                .orElse(new InternalApplicationException("Default TrustedParty is not registered in CPF-Store!"))))
        // TODO: Rollback if Organization update fail on NRO side.
        .delayUntil(this.organizationService::updateOrganization)
        .delayUntil(this.trustedPartyWebService::updateOrganization)
        .map(OrganizationResponseFactory::fromModel);
  }

  @Override
  public Mono<OrganizationResponseDTO> getOrganizationByIdentifier(String identifier) {
    return Mono.just(identifier)
        .delayUntil(MONO.makeSureNotNull(new BadRequestException("Request path variable 'identifier' can not be null or empty!")))
        .flatMap(this.organizationService::getOrganizationByIdentifier)
        .map(DTOFactory::toDTO);
  }
}
