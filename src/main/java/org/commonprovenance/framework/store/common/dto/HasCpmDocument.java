package org.commonprovenance.framework.store.common.dto;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.Optional;

import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.exceptions.InvalidValueException;
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

}
