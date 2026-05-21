package org.commonprovenance.framework.store.persistence.finalizedProvComponent;

import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;

import reactor.core.publisher.Mono;

public interface OrganizationPersistence {

  Mono<Void> create(Organization organization);

  Mono<Void> connectTrustedParty(Organization organization);

  Mono<Void> update(Organization organization);

  Mono<Organization> getByIdentifier(String identifier);

  Mono<Void> connectDocument(Document document);

}
