package org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasFormatSerialized;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasTokenNodeList;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.DocumentNode;

import io.vavr.control.Either;

public class DocumentNodeFactory {

  private static DocumentNode mapper(Document document) {
    return MonoidComposition.compose(
        new DocumentNode(),
        List.of(
            HasIdentifier.addIdentifier(document),
            HasGraph.addGraph(document),
            HasFormatSerialized.addFormat(document)));
  }

  public static Either<ApplicationException, DocumentNode> fromModel(Document document) {
    return Either.<ApplicationException, Document> right(document)
        .map(DocumentNodeFactory::mapper)
        .flatMap(EITHER::validateDTO);
  }

  public static Either<ApplicationException, DocumentNode> fromModelFull(Document document) {
    return Either.<ApplicationException, Document> right(document)
        .map(DocumentNodeFactory::mapper)
        .flatMap(HasTokenNodeList.addToken(document))
        .flatMap(EITHER::validateDTO);
  }

}
