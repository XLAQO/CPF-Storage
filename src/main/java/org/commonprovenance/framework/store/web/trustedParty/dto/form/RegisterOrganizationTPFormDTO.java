package org.commonprovenance.framework.store.web.trustedParty.dto.form;

import java.util.List;

import org.commonprovenance.framework.store.common.dto.HasClientCertificate;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasIntermediateCertificates;
import org.commonprovenance.framework.store.common.validation.ValidatableDTO;

public class RegisterOrganizationTPFormDTO extends ValidatableDTO implements
    HasIdentifier<RegisterOrganizationTPFormDTO>,
    HasClientCertificate<RegisterOrganizationTPFormDTO>,
    HasIntermediateCertificates<RegisterOrganizationTPFormDTO> {
  private final String organizationId;
  private final String clientCertificate;
  private final List<String> intermediateCertificates;

  public RegisterOrganizationTPFormDTO() {
    this.organizationId = null;
    this.clientCertificate = null;
    this.intermediateCertificates = null;
  }

  public RegisterOrganizationTPFormDTO(
      String organizationId,
      String clientCertificate,
      List<String> intermediateCertificates) {
    this.organizationId = organizationId;
    this.clientCertificate = clientCertificate;
    this.intermediateCertificates = intermediateCertificates;
  }

  @Override
  public RegisterOrganizationTPFormDTO withIdentifier(String identifier) {
    return new RegisterOrganizationTPFormDTO(
        identifier,
        this.getClientCertificate(),
        this.getIntermediateCertificates());
  }

  public String getIdentifier() {
    return organizationId;
  }

  public String getClientCertificate() {
    return clientCertificate;
  }

  public List<String> getIntermediateCertificates() {
    return intermediateCertificates;
  }

}
