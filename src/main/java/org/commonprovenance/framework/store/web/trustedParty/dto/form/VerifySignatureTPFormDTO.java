package org.commonprovenance.framework.store.web.trustedParty.dto.form;

import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasOrganizationId;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.common.validation.ValidatableDTO;

public class VerifySignatureTPFormDTO extends ValidatableDTO
    implements
    HasOrganizationId<VerifySignatureTPFormDTO>,
    HasGraph<VerifySignatureTPFormDTO>,
    HasSignature<VerifySignatureTPFormDTO> {
  private final String organizationId;
  private final String document;
  private final String signature;

  public VerifySignatureTPFormDTO() {
    this.organizationId = null;
    this.document = null;
    this.signature = null;
  }

  public VerifySignatureTPFormDTO(
      String organizationId,
      String document,
      String signature) {
    this.organizationId = organizationId;
    this.document = document;
    this.signature = signature;
  }

  @Override
  public VerifySignatureTPFormDTO withOrganizationId(String organizationId) {
    return new VerifySignatureTPFormDTO(
        organizationId,
        this.getGraph(),
        this.getSignature());
  }

  @Override
  public VerifySignatureTPFormDTO withGraph(String document) {
    return new VerifySignatureTPFormDTO(
        this.getOrganizationId(),
        document,
        this.getSignature());
  }

  public VerifySignatureTPFormDTO withSignature(String signature) {
    return new VerifySignatureTPFormDTO(
        this.getOrganizationId(),
        this.getGraph(),
        signature);
  }

  @Override
  public String getOrganizationId() {
    return organizationId;
  }

  @Override
  public String getGraph() {
    return document;
  }

  public String getSignature() {
    return signature;
  }
}
