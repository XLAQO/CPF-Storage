package org.commonprovenance.framework.store.controller.facade.impl;

import static org.commonprovenance.framework.store.common.publisher.PublisherHelper.MONO;

import org.commonprovenance.framework.store.config.AppConfiguration;
import org.commonprovenance.framework.store.controller.advice.ApplicationExceptionHandler;
import org.commonprovenance.framework.store.controller.dto.form.DocumentFormDTO;
import org.commonprovenance.framework.store.controller.facade.DocumentFacade;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.BadRequestException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.model.factory.DocumentFactory;
import org.commonprovenance.framework.store.model.utils.DocumentUtils;
import org.commonprovenance.framework.store.model.utils.OrganizationUtils;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.neo4j.TrustedPartyNeo4jRepository;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.DocumentService;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.OrganizationService;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.TrustedPartyService;
import org.commonprovenance.framework.store.service.web.trustedParty.TrustedPartyWebService;
import org.openprovenance.prov.model.ProvFactory;
import org.springframework.stereotype.Component;

import cz.muni.fi.cpm.model.ICpmFactory;
import cz.muni.fi.cpm.model.ICpmProvFactory;
import io.vavr.control.Either;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DocumentFacadeImpl implements DocumentFacade {
  private final String LOG_PREFIX = "DocumentFacade: ";
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFacadeImpl.class);

  private final DocumentService documentService;
  private final OrganizationService organizationService;
  private final TrustedPartyService trustedPartyService;

  private final TrustedPartyWebService trustedPartyWebService;

  private final ProvFactory provFactory;
  private final ICpmFactory cpmFactory;
  private final ICpmProvFactory cpmProvFactory;

  private final AppConfiguration configuration;

  public DocumentFacadeImpl(
      DocumentService documentService,
      OrganizationService organizationService,
      TrustedPartyService trustedPartyService,
      TrustedPartyWebService trustedPartyWebService,
      ProvFactory provFactory,
      ICpmFactory cpmFactory,
      ICpmProvFactory cpmProvFactory,
      AppConfiguration configuration) {
    this.documentService = documentService;
    this.organizationService = organizationService;
    this.trustedPartyService = trustedPartyService;

    this.trustedPartyWebService = trustedPartyWebService;

    this.provFactory = provFactory;
    this.cpmFactory = cpmFactory;
    this.cpmProvFactory = cpmProvFactory;

    this.configuration = configuration;

  }

  @Override
  public Mono<Void> createProvDocument(DocumentFormDTO body) {
    return Mono.just(body)
        .delayUntil(MONO.makeSureNotNull(new BadRequestException("Request body can not be null or empty!")))
        .map(DocumentFormDTO::getOrganizationIdentifier)
        .flatMap(organizationService::getOrganizationByIdentifier)
        .delayUntil(MONO.liftEffectToMono(OrganizationUtils::validateTrustedParty))
        .doOnNext(_ -> LOGGER.debug("{} Organization with TrustedParty has been loaded.", LOG_PREFIX))
        .flatMap(MONO.liftEffectToMono(organization -> Either.<ApplicationException, DocumentFormDTO> right(body)
            .map(DocumentFactory::fromFormDTO)
            .flatMap(document -> document.withCpmDocument(this.provFactory, this.cpmProvFactory, this.cpmFactory))
            .map(organization::withDocument)))
        .doOnNext(_ -> LOGGER.debug("{} Document has been deserialized and loaded.", LOG_PREFIX))
        .delayUntil(this.trustedPartyWebService::verifySignature)
        .doOnNext(_ -> LOGGER.debug("{} Signature has been verified.", LOG_PREFIX))
        .delayUntil(organization -> Mono.just(organization)
            .flatMap(MONO.liftOptionalToMono(
                Organization::getDocument,
                _ -> new InvalidValueException("Document has not been deserialized yet!")))
            .delayUntil(MONO.liftEffectToMono(DocumentUtils.checkBundleId(this.configuration)))
            // check document does not exists yet
            .delayUntil(this.documentService::checkDocumentDoesNotExists)
            // check connectors
            .delayUntil(this.documentService::checkBackwardConnectorsResolvable)
            .delayUntil(this.documentService::checkSpecForwardConnectorsResolvable)
            .delayUntil(MONO.liftEffectToMono(DocumentUtils::checkSpecForwardConnetorsAttrs))
            .delayUntil(MONO.liftEffectToMono(DocumentUtils::checkBackwardConnetorsAttrs))
            .delayUntil(MONO.liftEffectToMono(DocumentUtils::checkForwardConnetorsAttrs))
        // TODO: check hashes in connectors
        // TODO: check cpm constraints
        // TODO: check provenance constraints
        )
        .doOnNext(_ -> LOGGER.debug("Document has been validated and considered as valid."))
        .delayUntil(this.trustedPartyWebService::issueGraphToken)
        .doOnNext(_ -> LOGGER.debug("Token has been issued by TrustedParty."))

        .then();
  }

}
