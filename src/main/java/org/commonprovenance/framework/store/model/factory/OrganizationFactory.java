package org.commonprovenance.framework.store.model.factory;

import org.commonprovenance.framework.store.controller.dto.form.OrganizationFormDTO;
import org.commonprovenance.framework.store.model.Organization;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.OrganizationNode;

public class OrganizationFactory {
  public static Organization fromFormDTO(OrganizationFormDTO formDTO) {
    return new Organization(
        formDTO.getIdentifier(),
        formDTO.getClientCertificate(),
        formDTO.getIntermediateCertificates());
  }

  public static Organization fromPersistance(OrganizationNode node) {
    return new Organization(
        node.getIdentifier(),
        node.getClientCertificate(),
        node.getIntermediateCertificates());
  }

}
