package org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.factory;

import static org.commonprovenance.framework.store.common.utils.EitherUtils.EITHER;

import java.util.List;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasJwtToken;
import org.commonprovenance.framework.store.common.dto.HasTrustedPartyNodeList;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Token;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.model.node.TokenNode;

import io.vavr.control.Either;

public class TokenNodeFactory {

  private static TokenNode mapper(Token token) {
    return MonoidComposition.compose(
        new TokenNode(),
        List.of(HasJwtToken.addJwt(token)));
  }

  public static Either<ApplicationException, TokenNode> fromModel(Token token) {
    return Either.<ApplicationException, Token> right(token)
        .map(TokenNodeFactory::mapper)
        .flatMap(EITHER::validateDTO);
  }

  public static Either<ApplicationException, TokenNode> fromModelFull(Token token) {
    return Either.<ApplicationException, Token> right(token)
        .map(TokenNodeFactory::mapper)
        .flatMap(HasTrustedPartyNodeList.addTrustedParty(token))
        .flatMap(EITHER::validateDTO);
  }
}
