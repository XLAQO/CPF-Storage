package org.commonprovenance.framework.store.web.trustedParty.dto.form.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasDocumentOptional;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.VerifySignatureTPFormDTO;

import io.vavr.control.Either;

public class VerifySignatureFormFactory {
  private static <T extends HasIdentifier<T> & HasDocumentOptional<T>> UnaryOperator<VerifySignatureTPFormDTO> mapper(T data) {
    return MonoidComposition.<VerifySignatureTPFormDTO> composeOperators(
        List.of(
            HasIdentifier.addIdentifier(data),
            HasGraph.addGraph(data.getDocument()),
            HasSignature.addSignature(data.getDocument())));
  }

  public static Either<ApplicationException, VerifySignatureTPFormDTO> build(Organization organization) {
    return Either.<ApplicationException, VerifySignatureTPFormDTO> right(new VerifySignatureTPFormDTO())
        .map(VerifySignatureFormFactory.mapper(organization))
        .flatMap(EITHER::validateDTO);
  }
}
