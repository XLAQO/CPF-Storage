package org.commonprovenance.framework.store.controller.dto.form;

import java.util.List;
import java.util.Optional;

import org.commonprovenance.framework.store.common.dto.HasClientCertificate;
import org.commonprovenance.framework.store.common.dto.HasIdentifier;
import org.commonprovenance.framework.store.common.dto.HasIntermediateCertificates;
import org.commonprovenance.framework.store.common.dto.HasUrlOptional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "OrganizationForm", description = "Payload used to create or update an organization")
public class OrganizationFormDTO implements
    HasIdentifier<OrganizationFormDTO>,
    HasClientCertificate<OrganizationFormDTO>,
    HasIntermediateCertificates<OrganizationFormDTO>,
    HasUrlOptional<OrganizationFormDTO> {
  @Schema(description = "Organization identifier", example = "853226ba-9d56-4129-b51a-3b534f88957d", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Organization identifier should not be null or empty.")
  private final String identifier;

  @Schema(description = "PEM encoded client certificate", example = "-----BEGIN CERTIFICATE-----\n" +
      "MIIB8DCCAZWgAwIBAgIUbbA1CP+STZ240t1UH477j/tNMSQwCgYIKoZIzj0EAwIw\n" +
      "LjELMAkGA1UEBhMCQ1oxDDAKBgNVBAoMA0NQRjERMA8GA1UEAwwIY3BmLWludDIw\n" +
      "HhcNMjYwMzMxMTAyNDU1WhcNMjgwNzAzMTAyNDU1WjBKMQswCQYDVQQGEwJDWjEM\n" +
      "MAoGA1UECgwDQ1BGMS0wKwYDVQQDDCQ2ZmIyOTJhYS1lZTM4LTQ4YWUtOTk4Zi0w\n" +
      "NzlhZDlkMDFlN2MwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQ5ps+Ibs+yEoxc\n" +
      "rHRHiOTSblN0rD95161MgFNvf5rXbLTnjsOx9IsVVlKOGx9NH3+MV98D8Wm51YLI\n" +
      "wxUdjFVlo3UwczAMBgNVHRMBAf8EAjAAMA4GA1UdDwEB/wQEAwIHgDATBgNVHSUE\n" +
      "DDAKBggrBgEFBQcDAjAdBgNVHQ4EFgQUEwUuDicDuxN8Nhx9tCPZkliiYUAwHwYD\n" +
      "VR0jBBgwFoAUWuNDU3VuQ3L53GQJN0wHo5snWNUwCgYIKoZIzj0EAwIDSQAwRgIh\n" +
      "AJieCv0bUDa4H8vVtSOYZXMP9GN6aTMt5uxwwnbc+3egAiEAwe/uUGbedlPNWNfR\n" +
      "Xo4J8wBdiiMmYXa9MGU7TWVuuoU=\n" +
      "-----END CERTIFICATE-----", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Organization client certificate should not be null or empty.")
  private final String clientCertificate;

  @Schema(description = "PEM encoded intermediate certificates", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Organization intermediate certificates should not be null.")
  @NotEmpty(message = "Organization intermediate certificates should not be empty.")
  private final List<String> intermediateCertificates;

  @Schema(description = "Trusted party URL used by this organization", example = "http://trustedparty:8080/api/v1/", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private final String trustedPartyUri;

  @Schema(description = "Clearance period in seconds", example = "3600")
  private final Integer clearancePeriod;

  public OrganizationFormDTO(
      String identifier,
      String clientCertificate,
      List<String> intermediateCertificates,
      String trustedPartyUri,
      Integer clearancePeriod) {
    this.identifier = identifier;
    this.clientCertificate = clientCertificate;
    this.intermediateCertificates = intermediateCertificates;
    this.trustedPartyUri = trustedPartyUri;
    this.clearancePeriod = clearancePeriod;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getClientCertificate() {
    return clientCertificate;
  }

  public List<String> getIntermediateCertificates() {
    return intermediateCertificates;
  }

  public Optional<String> getUrl() {
    return Optional.ofNullable(trustedPartyUri);
  }

  public Optional<Integer> getClearancePeriod() {
    return Optional.ofNullable(clearancePeriod);
  }

}
