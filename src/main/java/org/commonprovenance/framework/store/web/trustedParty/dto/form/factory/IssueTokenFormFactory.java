package org.commonprovenance.framework.store.web.trustedParty.dto.form.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasCreatedOn;
import org.commonprovenance.framework.store.common.dto.HasFormatSerialized;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasGraphType;
import org.commonprovenance.framework.store.common.dto.HasOrganizationId;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.common.dto.HasTokenFormat;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.GraphType;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.IssueTokenTPFormDTO;

import io.vavr.control.Either;

public class IssueTokenFormFactory {

  private static Function<Document, IssueTokenTPFormDTO> mapper(Organization organization, GraphType graphType) {
    return (Document document) -> MonoidComposition.compose(
        new IssueTokenTPFormDTO(),
        List.of(
            HasOrganizationId.addIdentifier(organization),
            HasGraph.addGraph(document),
            HasFormatSerialized.addFormat(document),
            GraphType.GRAPH.equals(graphType) ? HasSignature.addSignature(document) : UnaryOperator.identity(),
            HasGraphType.addGraphType(graphType),
            HasCreatedOn.setCurrentTimeSecond(),
            HasTokenFormat.setJwtFormat()));
  }

  public static Either<ApplicationException, IssueTokenTPFormDTO> fromModel(Organization organization, GraphType graphType) {
    return Either.<ApplicationException, Organization> right(organization)
        .flatMap(EITHER.liftEitherOptional(
            Organization::getDocument,
            _ -> new InvalidValueException("Document has not been deserialized yet!")))
        .map(IssueTokenFormFactory.mapper(organization, graphType))
        .flatMap(EITHER::validateDTO);
  }
}
