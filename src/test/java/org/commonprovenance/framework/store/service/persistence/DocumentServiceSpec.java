package org.commonprovenance.framework.store.service.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.commonprovenance.framework.store.model.Document;
import org.commonprovenance.framework.store.model.Format;
import org.commonprovenance.framework.store.persistence.finalizedProvComponent.DocumentRepository;
import org.commonprovenance.framework.store.service.persistence.finalizedProvComponent.impl.DocumentServiceImpl;
import org.commonprovenance.framework.store.service.web.store.StoreWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service - DocumentServiceImpl Specification")
class DocumentServiceSpec {

  @Mock
  private DocumentRepository documentRepository;
  @Mock
  private StoreWebService storeWebService;

  private DocumentServiceImpl documentService;

  private final String UUID_1 = "e3cf8742-b595-47f4-8aae-a1e94b62a856";
  private final String TEST_ORG_ID_1 = "6ee9d79b-0615-4cb1-b0f3-2303d10c8cff";
  private final String BASE64_STRING_GRAPH_1 = "AAAAQQAAAGIAAAByAAAAYQAAAGsAAABhAAAAIAAAAEQAAABhAAAAYgAAAHIAAABhAAAALgAAAC4=";
  private final Format FORMAT_1 = Format.JSON;

  private final String SIGNATURE = "...";

  @BeforeEach
  void setUp() {
    documentService = new DocumentServiceImpl(documentRepository, storeWebService);
  }

  @Test
  @DisplayName("storeDocument - should call create on repository once with exact Document.")

  void storeDocument_should_call_create_on_repository() {
    Document document = new Document(UUID_1, TEST_ORG_ID_1, BASE64_STRING_GRAPH_1, FORMAT_1, SIGNATURE);

    when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> {
      return Mono.empty();
    });

    StepVerifier.create(documentService.storeDocument(document))
        .verifyComplete();

    ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
    verify(
        documentRepository,
        times(1)
            .description("Repository save method should be invoked once"))
        .save(captor.capture());

    Document capturedEntity = captor.getValue();
    assertTrue(capturedEntity.getIdentifier().get().equals(UUID_1)
        && capturedEntity.getGraph().equals(BASE64_STRING_GRAPH_1)
        && capturedEntity.getFormat().map(FORMAT_1::equals).orElse(false),
        "should be called with exact Document");
  }

  @Test
  void getDocumentByIdentifier_shouldReturnDocument() {
    Document document = new Document(UUID_1, TEST_ORG_ID_1, BASE64_STRING_GRAPH_1, FORMAT_1, SIGNATURE);
    when(documentRepository.findByIdentifier(UUID_1)).thenReturn(Mono.just(document));

    StepVerifier.create(documentService.getDocumentByIdentifier(UUID_1))
        .expectNext(document)
        .verifyComplete();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(
        documentRepository,
        times(1)
            .description("Repository getByIdentifier method should be invoked once"))
        .findByIdentifier(captor.capture());

    String capturedId = captor.getValue();
    assertTrue(capturedId.equals(UUID_1),
        "should be called with exact id");
  }

}
