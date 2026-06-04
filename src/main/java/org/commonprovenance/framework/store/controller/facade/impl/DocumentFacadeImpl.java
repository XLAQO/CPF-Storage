package org.commonprovenance.framework.store.controller.facade.impl;

import static org.commonprovenance.framework.store.common.publisher.PublisherHelper.MONO;
import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import org.commonprovenance.framework.store.config.AppConfiguration;
import org.commonprovenance.framework.store.controller.dto.form.DocumentFormDTO;
import org.commonprovenance.framework.store.controller.dto.response.TokenResponseDTO;
import org.commonprovenance.framework.store.controller.dto.response.factory.TokenResponseFactory;
import org.commonprovenance.framework.store.controller.facade.DocumentFacade;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.BadRequestException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
import org.commonprovenance.framework.store.exceptions.NotFoundException;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.model.factory.DocumentFactory;
import org.commonprovenance.framework.store.model.utils.DocumentUtils;
import org.commonprovenance.framework.store.model.utils.OrganizationUtils;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.DocumentService;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.OrganizationService;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.TrustedPartyService;
import org.commonprovenance.framework.store.service.persistence.metaComponent.MetaProvenanceComponentService;
import org.commonprovenance.framework.store.service.web.trustedParty.TrustedPartyWebService;
import org.openprovenance.prov.model.ProvFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cz.muni.fi.cpm.model.ICpmFactory;
import cz.muni.fi.cpm.model.ICpmProvFactory;
import io.vavr.control.Either;
import reactor.core.publisher.Mono;

@Component
public class DocumentFacadeImpl implements DocumentFacade {
  private final String LOG_PREFIX = "DocumentFacade: ";
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFacadeImpl.class);

  private final DocumentService documentService;
  private final OrganizationService organizationService;
  private final TrustedPartyService trustedPartyService;

  private final MetaProvenanceComponentService metaComponentService;

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
      MetaProvenanceComponentService metaComponentService,
      ProvFactory provFactory,
      ICpmFactory cpmFactory,
      ICpmProvFactory cpmProvFactory,
      AppConfiguration configuration) {
    this.documentService = documentService;
    this.organizationService = organizationService;
    this.trustedPartyService = trustedPartyService;

    this.metaComponentService = metaComponentService;
    this.trustedPartyWebService = trustedPartyWebService;

    this.provFactory = provFactory;
    this.cpmFactory = cpmFactory;
    this.cpmProvFactory = cpmProvFactory;

    this.configuration = configuration;

  }

  @Override
  public Mono<TokenResponseDTO> createProvDocument(String organizationIdentifier, DocumentFormDTO body) {
    return Mono.just(organizationIdentifier)
        .delayUntil(MONO.makeSureNotNull(new BadRequestException("Organization identifier can not be null or empty!")))
        .flatMap(organizationService::getOrganizationByIdentifier)
        .delayUntil(MONO.liftEffectToMono(OrganizationUtils::validateTrustedParty))
        .doOnNext(_ -> LOGGER.debug("{} Organization with TrustedParty has been loaded.", LOG_PREFIX))
        .flatMap(MONO.liftEffectToMono(organization -> Either.<ApplicationException, DocumentFormDTO> right(body)
            .flatMap(EITHER.makeSureNotNull(_ -> new BadRequestException("Request body can not be null or empty!")))
            .map(DocumentFactory::build)
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
        .delayUntil(this.organizationService::storeDocument)
        .doOnNext(_ -> LOGGER.debug("Document has been saved."))
        .delayUntil(this.metaComponentService::createMetaProvenanceComponentIfNotExists)
        .delayUntil(this.metaComponentService::addBundleVersionIntoMetaProvenanceComponent)
        .delayUntil(this.metaComponentService::addTokenIntoMetaProvenanceComponent)

        .doOnNext(_ -> LOGGER.debug("MetaComponent stored"))
        .flatMap(MONO.liftEffectToMono(TokenResponseFactory::build))
        .doOnNext(_ -> LOGGER.debug("Finito.."));
  }

  @Override
  public Mono<Void> exists(String identifier) {
    return Mono.justOrEmpty(identifier)
        .flatMap(MONO.makeSureAsync(
            this.documentService::existsByIdentifier,
            id -> new NotFoundException("Document with identifier '" + id + "' does not exist!")))
        .then();
  }

}
