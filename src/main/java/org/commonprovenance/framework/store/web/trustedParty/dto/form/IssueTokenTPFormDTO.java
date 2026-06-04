package org.commonprovenance.framework.store.web.trustedParty.dto.form;

import java.time.Instant;

import org.commonprovenance.framework.store.common.dto.HasCreatedOn;
import org.commonprovenance.framework.store.common.dto.HasFormatSerialized;
import org.commonprovenance.framework.store.common.dto.HasGraph;
import org.commonprovenance.framework.store.common.dto.HasGraphType;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasSignature;
import org.commonprovenance.framework.store.common.dto.HasTokenFormat;
import org.commonprovenance.framework.store.common.validation.ValidatableDTO;
import org.commonprovenance.framework.store.model.Format;
import org.commonprovenance.framework.store.model.GraphType;

public class IssueTokenTPFormDTO extends ValidatableDTO implements
    HasIdentifier<IssueTokenTPFormDTO>,
    HasGraph<IssueTokenTPFormDTO>,
    HasFormatSerialized<IssueTokenTPFormDTO>,
    HasSignature<IssueTokenTPFormDTO>,
    HasGraphType<IssueTokenTPFormDTO>,
    HasCreatedOn<IssueTokenTPFormDTO>,
    HasTokenFormat<IssueTokenTPFormDTO> {
  private final String organizationId;
  private final String document;
  private final String documentFormat;
  private final String signature;
  private final String type;
  private final Long createdOn;
  private final String tokenFormat;

  public IssueTokenTPFormDTO() {
    this.organizationId = null;
    this.document = null;
    this.documentFormat = null;
    this.signature = null;
    this.type = null;
    this.createdOn = Instant.now().getEpochSecond();
    this.tokenFormat = "jwt";
  }

  public IssueTokenTPFormDTO(
      String organizationId,
      String document,
      String documentFormat,
      String signature,
      String type,
      Long createdOn,
      String tokenFormat) {
    this.organizationId = organizationId;
    this.document = document;
    this.documentFormat = documentFormat;
    this.signature = signature;
    this.type = type;
    this.createdOn = createdOn;
    this.tokenFormat = tokenFormat != null ? tokenFormat : "jwt";
  }

  @Override
  public IssueTokenTPFormDTO withIdentifier(String identifier) {
    return new IssueTokenTPFormDTO(
        identifier,
        this.getGraph(),
        this.getDocumentFormat(),
        this.getSignature(),
        this.getGraphType(),
        this.getCreatedOn(),
        this.getTokenFormat());
  }

  @Override
  public IssueTokenTPFormDTO withGraph(String document) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        document,
        this.getDocumentFormat(),
        this.getSignature(),
        this.getGraphType(),
        this.getCreatedOn(),
        this.getTokenFormat());
  }

  public IssueTokenTPFormDTO withDocumentFormat(Format format) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        this.getGraph(),
        format.toString().toLowerCase(),
        this.getSignature(),
        this.getGraphType(),
        this.getCreatedOn(),
        this.getTokenFormat());
  }

  public IssueTokenTPFormDTO withSignature(String signature) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        this.getGraph(),
        this.getDocumentFormat(),
        signature,
        this.getGraphType(),
        this.getCreatedOn(),
        this.getTokenFormat());
  }

  public IssueTokenTPFormDTO withGraphType(GraphType graphType) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        this.getGraph(),
        this.getDocumentFormat(),
        this.getSignature(),
        graphType.toString().toLowerCase(),
        this.getCreatedOn(),
        this.getTokenFormat());
  }

  public IssueTokenTPFormDTO withCreatedOn(Long createdOn) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        this.getGraph(),
        this.getDocumentFormat(),
        this.getSignature(),
        this.getGraphType(),
        createdOn,
        this.getTokenFormat());
  }

  public IssueTokenTPFormDTO withTokenFormat(String tokenFormat) {
    return new IssueTokenTPFormDTO(
        this.getIdentifier(),
        this.getGraph(),
        this.getDocumentFormat(),
        this.getSignature(),
        this.getGraphType(),
        this.getCreatedOn(),
        tokenFormat);
  }

  @Override
  public String getIdentifier() {
    return organizationId;
  }

  public String getGraph() {
    return document;
  }

  public String getDocumentFormat() {
    return documentFormat;
  }

  public String getSignature() {
    return signature;
  }

  public String getGraphType() {
    return type;
  }

  public Long getCreatedOn() {
    return createdOn;
  }

  public String getTokenFormat() {
    return tokenFormat;
  }
}
