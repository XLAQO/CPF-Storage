package org.commonprovenance.framework.store.web.trustedParty.dto.form.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasOrganizationId;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.RegisterOrganizationTPFormDTO;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.UpdateOrganizationTPFormDTO;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.VerifySignatureTPFormDTO;

import io.vavr.control.Either;

public class DTOFactory {

  // ---

  public static Either<ApplicationException, RegisterOrganizationTPFormDTO> toForm(Organization organization) {
    return Either.<ApplicationException, RegisterOrganizationTPFormDTO> right(new RegisterOrganizationTPFormDTO(
        organization.getIdentifier(),
        organization.getClientCertificate(),
        organization.getIntermediateCertificates()))
        .flatMap(EITHER::validateDTO);
  }

  public static Either<ApplicationException, UpdateOrganizationTPFormDTO> toUpdateForm(Organization organization) {
    return Either.<ApplicationException, UpdateOrganizationTPFormDTO> right(new UpdateOrganizationTPFormDTO(
        organization.getClientCertificate(),
        organization.getIntermediateCertificates()))
        .flatMap(EITHER::validateDTO);
  }

  public static Either<ApplicationException, VerifySignatureTPFormDTO> toForm(Organization organization, Document document) {
    return Either.<ApplicationException, VerifySignatureTPFormDTO> right(MonoidComposition.compose(
        new VerifySignatureTPFormDTO(),
        List.of(
            HasOrganizationId.addIdentifier(organization),
            HasGraph.addGraph(document),
            HasSignature.addSignature(document))))
        .flatMap(EITHER::validateDTO);
  }
}
