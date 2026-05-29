package org.commonprovenance.framework.store.model.factory;

import org.commonprovenance.framework.store.model.TrustedParty;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.TrustedPartyNode;

public class TrustedPartyFactory {
  public static TrustedParty fromPersistance(TrustedPartyNode node) {
    return new TrustedParty(
        node.getName(),
        node.getClientCertificate(),
        node.getUrl(),
        node.getIsChecked(),
        node.getIsValid(),
        node.getIsDefault())
        .withId(node.getId());
  }

}
