package org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasClientCertificate;
import org.commonprovenance.framework.store.common.dto.HasIsChecked;
import org.commonprovenance.framework.store.common.dto.HasIsDefault;
import org.commonprovenance.framework.store.common.dto.HasIsValid;
import org.commonprovenance.framework.store.common.dto.HasName;
import org.commonprovenance.framework.store.common.dto.HasUrl;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.TrustedParty;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.TrustedPartyNode;

import io.vavr.control.Either;

public class TrustedPartyNodeFactory {

  private static TrustedPartyNode mapper(TrustedParty trustedParty) {
    return MonoidComposition.compose(
        new TrustedPartyNode(),
        List.of(
            HasName.addName(trustedParty),
            HasClientCertificate.addClientCertificate(trustedParty),
            HasUrl.addUrl(trustedParty),
            HasIsChecked.addIsChecked(trustedParty),
            HasIsValid.addIsValid(trustedParty),
            HasIsDefault.addIsDefault(trustedParty)));
  }

  public static Either<ApplicationException, TrustedPartyNode> fromModel(TrustedParty trustedParty) {
    return Either.<ApplicationException, TrustedParty> right(trustedParty)
        .map(TrustedPartyNodeFactory::mapper)
        .flatMap(EITHER::validateDTO);
  }

}
