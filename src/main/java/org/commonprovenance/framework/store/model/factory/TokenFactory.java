package org.commonprovenance.framework.store.model.factory;

import java.util.List;
import java.util.function.Function;

import org.commonprovenance.framework.store.common.composition.MonoidComposition;
import org.commonprovenance.framework.store.common.dto.HasJwtToken;
import org.commonprovenance.framework.store.common.dto.HasTrustedPartyNodeList;
import org.commonprovenance.framework.store.common.dto.HasTrustedPartyOptional;
import org.commonprovenance.framework.store.exceptions.ApplicationException;
import org.commonprovenance.framework.store.model.Token;

import io.vavr.control.Either;

public class TokenFactory {

  private static <T extends HasJwtToken<T>> Function<Token, Either<ApplicationException, Token>> mapper(T data) {
    return (Token token) -> Either.<ApplicationException, Token> right(MonoidComposition.compose(
        token,
        List.of(HasJwtToken.addJwt(data))))
        .flatMap(HasJwtToken::loadCreatedOn);
  }

  public static <T extends HasJwtToken<T>> Either<ApplicationException, Token> build(T data) {
    return Either.<ApplicationException, Token> right(new Token())
        .flatMap(TokenFactory.mapper(data));

  }

  public static <T extends HasJwtToken<T> & HasTrustedPartyNodeList<T>> Either<ApplicationException, Token> buildWithRelations(T value) {
    return build(value)
        .flatMap(HasTrustedPartyOptional.addTrustedParty(value));
  }

}
