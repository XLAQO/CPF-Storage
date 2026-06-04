package org.commonprovenance.framework.store.web.trustedParty.dto.form;

import java.util.List;

import org.commonprovenance.framework.store.common.dto.HasClientCertificate;
import org.commonprovenance.framework.store.common.dto.HasIntermediateCertificates;
import org.commonprovenance.framework.store.common.validation.ValidatableDTO;

public class UpdateOrganizationTPFormDTO extends ValidatableDTO implements
    HasClientCertificate<UpdateOrganizationTPFormDTO>,
    HasIntermediateCertificates<UpdateOrganizationTPFormDTO> {
  private final String clientCertificate;
  private final List<String> intermediateCertificates;

  public UpdateOrganizationTPFormDTO() {
    this.clientCertificate = null;
    this.intermediateCertificates = null;
  }

  public UpdateOrganizationTPFormDTO(
      String clientCertificate,
      List<String> intermediateCertificates) {
    this.clientCertificate = clientCertificate;
    this.intermediateCertificates = intermediateCertificates;
  }

  public String getClientCertificate() {
    return clientCertificate;
  }

  public List<String> getIntermediateCertificates() {
    return intermediateCertificates;
  }
}
