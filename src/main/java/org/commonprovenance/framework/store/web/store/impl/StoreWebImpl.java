package org.commonprovenance.framework.store.web.store.impl;

import org.commonprovenance.framework.store.exceptions.InternalApplicationException;
import org.commonprovenance.framework.store.exceptions.NotFoundException;
import org.commonprovenance.framework.store.exceptions.factory.ApplicationExceptionFactory;
import org.commonprovenance.framework.store.web.store.StoreWeb;
import org.commonprovenance.framework.store.web.store.client.ClientStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class StoreWebImpl implements StoreWeb {
  private final String LOG_PREFIX = "StoreWebImpl: ";
  private static final Logger LOGGER = LoggerFactory.getLogger(StoreWebImpl.class);

  private final ClientStore client;

  public StoreWebImpl(ClientStore client) {
    this.client = client;
  }

  @Override
  public Mono<Void> pingByUrl(String url) {
    return Mono.just(url)
        .flatMap(this.client::sendHeadRequest)
        .doOnSuccess(_ -> LOGGER.trace(LOG_PREFIX + "Ping to Resource at url '" + url + "' has been passed."))
        .doOnError(throwable -> {
          if (throwable instanceof NotFoundException notFound)
            LOGGER.trace(LOG_PREFIX + notFound.getMessage());
          else
            LOGGER.error(LOG_PREFIX + "Ping to Resource at url '" + url + "' has been failed!\n" + throwable.getMessage());
        })
        .onErrorMap(ApplicationExceptionFactory.handleThrowable(new InternalApplicationException("Ping to Resource at url '" + url + "' has been failed!")));

  }

}
