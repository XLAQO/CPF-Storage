package org.commonprovenance.framework.store.common.dto;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
import org.commonprovenance.framework.store.model.utils.DocumentUtils;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.QualifiedName;

import cz.muni.fi.cpm.model.CpmDocument;
import io.vavr.control.Either;

public interface HasCpmDocument<T extends HasCpmDocument<T>> {
  Either<ApplicationException, CpmDocument> getCpmDocument();

  default Either<ApplicationException, String> getIdentifier() {
    return getCpmDocument()
        .map(CpmDocument::getBundleId)
        .map(QualifiedName::getLocalPart);
  }

  default Either<ApplicationException, Void> checkSpecForwardConnetorsAttrs() {
    return getCpmDocument()
        .flatMap(DocumentUtils::getSpecForwardConnectors)
        .flatMap(EITHER.traverseEither(EITHER.<Entity> makeSure(
            DocumentUtils::isValidSpecForwardConnector,
            InvalidValueException::new,
            element -> "Entity '" + element.getId() + "' is not valid specialized forward connector")))
        .mapToVoid();
  }

}
