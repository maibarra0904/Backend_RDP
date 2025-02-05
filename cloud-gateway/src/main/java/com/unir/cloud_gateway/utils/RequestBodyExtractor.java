package com.unir.cloud_gateway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.cloud_gateway.model.GatewayRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.util.Strings;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class RequestBodyExtractor {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public GatewayRequest getRequest(ServerWebExchange exchange, DataBuffer buffer) {
    DataBufferUtils.retain(buffer);
    Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(buffer.split(buffer.readableByteCount())));
    String rawRequest = getRawRequest(cachedFlux);
    DataBufferUtils.release(buffer);
    GatewayRequest request = objectMapper.readValue(rawRequest, GatewayRequest.class);
    request.setExchange(exchange);

    // Set headers -- Needed: https://github.com/spring-cloud/spring-cloud-gateway/issues/894
    HttpHeaders headers = new HttpHeaders();
    headers.putAll(exchange.getRequest().getHeaders());
    headers.remove(HttpHeaders.CONTENT_LENGTH);
    headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
    request.setHeaders(headers);
    return request;
  }

  /**
   * This method converts a Flux of DataBuffers into a raw request string.
   * It does this by subscribing to the Flux of DataBuffers, reading the bytes from each DataBuffer, and converting the bytes into a string.
   *
   * @param body the Flux of DataBuffers representing the body of the request
   * @return a string representing the raw request
   */
  private String getRawRequest(Flux<DataBuffer> body) {
    AtomicReference<String> rawRef = new AtomicReference<>();
    body.subscribe(buffer -> {
      byte[] bytes = new byte[buffer.readableByteCount()];
      buffer.read(bytes);
      rawRef.set(Strings.fromUTF8ByteArray(bytes));
    });
    return rawRef.get();
  }

}
