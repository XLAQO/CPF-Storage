package org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.commonprovenance.framework.store.common.dto.HasId;
import org.commonprovenance.framework.store.common.dto.HasJwtToken;
import org.commonprovenance.framework.store.common.dto.HasTrustedPartyNodeList;
import org.commonprovenance.framework.store.common.validation.ValidatableDTO;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.relation.WasIssuedBy;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Token")
public class TokenNode extends ValidatableDTO implements
    HasId,
    HasJwtToken<TokenNode>,
    HasTrustedPartyNodeList<TokenNode> {
  @Id
  @GeneratedValue
  private final String id;

  private final String jwt;

  @Relationship(type = "was_issued_by", direction = Relationship.Direction.OUTGOING)
  private final List<WasIssuedBy> wasIssuedBy;

  // Constructor for full initialization (used by Neo4j when reading)
  @PersistenceCreator
  public TokenNode(
      String id,
      String jwt,
      List<WasIssuedBy> wasIssuedBy) {
    this.id = id;
    this.jwt = jwt;
    this.wasIssuedBy = wasIssuedBy;
  }

  // Constructor for creating new node (id will be generated)
  public TokenNode() {
    this.id = null;
    this.jwt = null;
    this.wasIssuedBy = Collections.emptyList();
  }

  public TokenNode(String jwt) {
    this.id = null;
    this.jwt = jwt;
    this.wasIssuedBy = Collections.emptyList();
  }

  // Factory methods
  public TokenNode withJwt(String jwtToken) {
    return new TokenNode(
        this.getId(),
        jwtToken,
        this.getWasIssuedBy());
  }

  public TokenNode withTrustedParty(TrustedPartyNode trustedPartyEntity) {
    if (trustedPartyEntity == null) {
      return this;
    }

    List<WasIssuedBy> updatedWasIssuedBy = Stream.concat(
        this.getWasIssuedBy().stream(),
        Stream.of(new WasIssuedBy(trustedPartyEntity)))
        .collect(Collectors.toList());

    return new TokenNode(
        this.getId(),
        this.getJwt(),
        updatedWasIssuedBy);
  }

  // Wither method for Neo4j to set relationships
  public TokenNode withWasIssuedBy(List<WasIssuedBy> wasIssuedBy) {
    return new TokenNode(
        this.getId(),
        this.getJwt(),
        wasIssuedBy);
  }

  public String getId() {
    return this.id;
  }

  public String getJwt() {
    return jwt;
  }

  public List<WasIssuedBy> getWasIssuedBy() {
    return wasIssuedBy;
  }

  public List<TrustedPartyNode> getTrustedParties() {
    return this.getWasIssuedBy().stream()
        .map(WasIssuedBy::getTrustedParty)
        .collect(Collectors.toList());
  }

}
