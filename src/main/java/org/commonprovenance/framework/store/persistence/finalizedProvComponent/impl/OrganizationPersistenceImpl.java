package org.commonprovenance.framework.store.persistence.finalizedProvComponent.impl;

import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.OrganizationPersistence;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.repository.OrganizationRepository;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class OrganizationPersistenceImpl implements OrganizationPersistence {

  private final OrganizationRepository repository;

  public OrganizationPersistenceImpl(
      OrganizationRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Void> create(Organization organization) {
    return repository.save(organization);
  }

  @Override
  public Mono<Void> connectTrustedParty(Organization organization) {
    return repository.connectTrusts(organization);
  }

  @Override
  public Mono<Void> update(Organization organization) {
    return repository.save(organization);
  }

  @Override
  public Mono<Organization> getByIdentifier(String identifier) {
    return repository.findByIdentifier(identifier);

  }

  @Override
  public Mono<Void> connectDocument(Document document) {
    return repository.connectOwns(document);
  }

}
