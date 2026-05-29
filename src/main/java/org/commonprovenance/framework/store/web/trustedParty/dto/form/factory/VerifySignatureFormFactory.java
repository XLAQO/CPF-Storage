package org.commonprovenance.framework.store.web.trustedParty.dto.form.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;
import java.util.function.Function;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasOrganizationId;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.VerifySignatureTPFormDTO;

import io.vavr.control.Either;

public class VerifySignatureFormFactory {
  private static Function<Document, VerifySignatureTPFormDTO> mapper(Organization organization) {
    return (Document document) -> MonoidComposition.compose(
        new VerifySignatureTPFormDTO(),
        List.of(
            HasOrganizationId.addIdentifier(organization),
            HasGraph.addGraph(document),
            HasSignature.addSignature(document)));
  }

  public static Either<ApplicationException, VerifySignatureTPFormDTO> fromModel(Organization organization) {
    return Either.<ApplicationException, Organization> right(organization)
        .flatMap(EITHER.liftEitherOptional(
            Organization::getDocument,
            _ -> new InvalidValueException("Document has not been deserialized yet!")))
        .map(VerifySignatureFormFactory.mapper(organization))
        .flatMap(EITHER::validateDTO);
  }
}
