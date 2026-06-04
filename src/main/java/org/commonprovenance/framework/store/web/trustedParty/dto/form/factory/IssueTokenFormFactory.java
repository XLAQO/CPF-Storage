package org.commonprovenance.framework.store.web.trustedParty.dto.form.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;
import java.util.function.UnaryOperator;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasCreatedOn;
import org.commonprovenance.framework.store.common.dto.HasDocumentOptional;
import org.commonprovenance.framework.store.common.dto.HasFormatSerialized;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasGraphType;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.common.dto.HasTokenFormat;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.GraphType;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.web.trustedParty.dto.form.IssueTokenTPFormDTO;

import io.vavr.control.Either;

public class IssueTokenFormFactory {

  private static <T extends HasIdentifier<T> & HasDocumentOptional<T>> UnaryOperator<IssueTokenTPFormDTO> mapper(T data, GraphType graphType) {
    return MonoidComposition.<IssueTokenTPFormDTO> composeOperators(
        List.of(
            HasIdentifier.addIdentifier(data),
            HasGraph.addGraph(data.getDocument()),
            HasFormatSerialized.addFormat(data.getDocument()),
            graphType.equals(GraphType.GRAPH) ? HasSignature.addSignature(data.getDocument()) : UnaryOperator.identity(),
            HasGraphType.addGraphType(graphType),
            HasCreatedOn.setCurrentTimeSecond(),
            HasTokenFormat.setJwtFormat()));
  }

  public static Either<ApplicationException, IssueTokenTPFormDTO> fromModel(Organization organization, GraphType graphType) {
    return Either.<ApplicationException, IssueTokenTPFormDTO> right(new IssueTokenTPFormDTO())
        .map(IssueTokenFormFactory.mapper(organization, graphType))
        .flatMap(EITHER::validateDTO);
  }
}
