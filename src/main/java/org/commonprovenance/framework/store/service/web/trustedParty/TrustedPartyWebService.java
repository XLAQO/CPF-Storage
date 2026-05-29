package org.commonprovenance.framework.store.service.web.trustedParty;

import java.util.Optional;
import java.util.function.Function;

import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.model.Token;

import reactor.core.publisher.Mono;

public interface TrustedPartyWebService {
  Mono<Void> registerOrganization(Organization organization);

  Mono<Void> updateOrganization(Organization organization);

  Mono<Boolean> organizationIsRegistered(Organization organization);

  Mono<Boolean> organizationIsNotRegistered(Organization organization);

  Function<Organization, Mono<Organization>> setTrustedPartyByBaseUrl(Optional<String> optTrustedPartyBaseUrl);

  Mono<Void> verifySignature(Organization organization);

  Mono<Organization> issueGraphToken(Organization organization);

  Mono<Token> issueGraphToken(Document document);

  Function<Document, Mono<Token>> issueDomainSpecificGraphToken(Optional<String> trustedPartyUrl);

  Function<Document, Mono<Token>> issueBackboneGraphToken(Optional<String> trustedPartyUrl);
}
