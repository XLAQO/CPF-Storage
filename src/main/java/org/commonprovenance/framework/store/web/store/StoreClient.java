package org.commonprovenance.framework.store.web.store;

import reactor.core.publisher.Mono;

public interface StoreClient {

  Mono<Void> pingByUrl(String url);
}
