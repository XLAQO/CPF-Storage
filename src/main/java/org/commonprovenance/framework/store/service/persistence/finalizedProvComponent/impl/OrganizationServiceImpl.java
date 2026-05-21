package org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.impl;

import static org.commonprovenance.framework.store.common.publisher.PublisherHelper.MONO;

import org.commonprovenance.framework.store.exceptions.ConflictException;
import org.commonprovenance.framework.store.exceptions.NotFoundException;
import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.OrganizationRepository;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.OrganizationService;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationRepository repository;

  public OrganizationServiceImpl(OrganizationRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Void> storeOrganization(Organization organization) {
    return MONO.<Organization> makeSureNotNullWithMessage("Organization can not be null")
        .apply(organization)
        .delayUntil(this.repository::save)
        .delayUntil(this.repository::connectTrusts)
        .then();
  }

  @Override
  public Mono<Void> updateOrganization(Organization organization) {
    return MONO.<Organization> makeSureNotNullWithMessage("Organization can not be null")
        .apply(organization)
        .flatMap(this.repository::save);
  }

  @Override
  public Mono<Boolean> exists(Organization organization) {
    return MONO.<Organization> makeSureNotNullWithMessage("Organization can not be null")
        .apply(organization)
        .map(Organization::getIdentifier)
        .flatMap(this::getOrganizationByIdentifier)
        .thenReturn(true)
        .onErrorResume(NotFoundException.class, _ -> Mono.just(false));
  }

  @Override
  public Mono<Boolean> notExists(Organization organization) {
    return exists(organization).map(result -> !result);
  }

  @Override
  public Mono<Void> checkOrganizationDoesNotExists(Organization organization) {
    return Mono.just(organization)
        .flatMap(MONO.makeSureAsync(
            this::notExists,
            ConflictException::new,
            org -> "Organization with identifier " + org.getIdentifier() + " already exists!"))
        .then();
  }

  @Override
  public Mono<Void> checkOrganizationExists(Organization organization) {
    return Mono.just(organization)
        .flatMap(MONO.makeSureAsync(
            this::exists,
            ConflictException::new,
            org -> "Organization with identifier " + org.getIdentifier() + " has not been registered yet!"))
        .then();
  }

  @Override
  public Mono<Organization> getOrganizationByIdentifier(String identifier) {
    return this.repository.findByIdentifier(identifier);
  }

  @Override
  public Mono<Organization> getOrganization(Organization organization) {
    return MONO.<Organization> makeSureNotNullWithMessage("Organization can not be null")
        .apply(organization)
        .map(Organization::getIdentifier)
        .flatMap(this::getOrganizationByIdentifier);
  }

  @Override
  public Mono<Void> linkOwnedDocument(Document document) {
    return this.repository.connectOwns(document);
  }

}
