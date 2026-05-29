package org.commonprovenance.framework.store.controller.dto.response.factory;

import org.commonprovenance.framework.store.controller.dto.response.OrganizationResponseDTO;
import org.commonprovenance.framework.store.model.Organization;

public class OrganizationResponseFactory {
  public static OrganizationResponseDTO fromModel(Organization model) {
    return new OrganizationResponseDTO(
        model.getIdentifier(),
        model.getClientCertificate(),
        model.getIntermediateCertificates());
  }
}
