package org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasClientCertificate;
import org.commonprovenance.framework.store.common.dto.HasDocumentNodeList;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasIntermediateCertificates;
import org.commonprovenance.framework.store.common.dto.HasTrustedPartyNodeList;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.OrganizationNode;

import io.vavr.control.Either;

public class OrganizationNodeFactory {

  private static OrganizationNode mapper(Organization organization) {
    return MonoidComposition.compose(
        new OrganizationNode(),
        List.of(
            HasIdentifier.addIdentifier(organization),
            HasClientCertificate.addClientCertificate(organization),
            HasIntermediateCertificates.addIntermediateCertificates(organization)));
  }

  public static Either<ApplicationException, OrganizationNode> fromModel(Organization organization) {
    return Either.<ApplicationException, Organization> right(organization)
        .map(OrganizationNodeFactory::mapper)
        .flatMap(EITHER::validateDTO);
  }

  public static Either<ApplicationException, OrganizationNode> fromModelFull(Organization organization) {
    return Either.<ApplicationException, Organization> right(organization)
        .map(OrganizationNodeFactory::mapper)
        .flatMap(HasTrustedPartyNodeList.addTrustedParty(organization))
        .flatMap(HasDocumentNodeList.addDocument(organization))
        .flatMap(EITHER::validateDTO);
  }
}
