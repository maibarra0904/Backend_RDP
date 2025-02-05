package com.unir.cloud_gateway.filter;

import com.unir.cloud_gateway.decorator.RequestDecoratorFactory;
import com.unir.cloud_gateway.model.GatewayRequest;
import com.unir.cloud_gateway.utils.RequestBodyExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestTranslationFilter implements GlobalFilter {

  private final RequestBodyExtractor requestBodyExtractor;
  private final RequestDecoratorFactory requestDecoratorFactory;

  /**
   * This method is the main filter method for the Spring Cloud Gateway.
   * It checks if the incoming request has a content type and is a POST request.
   * If the request does have a content type and is a POST request, the body of the request is joined into a single DataBuffer.
   * Then, the request is mutated using the decorator before being forwarded.
   * By default, the response status is set to 400 (Bad Request). This will be overridden if the request is valid.
   *
   * @param exchange the current server web exchange
   * @param chain    the gateway filter chain
   * @return a Mono<Void> that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(
      ServerWebExchange exchange,
      GatewayFilterChain chain) {

    // By default, set the response status to 400. This will be overridden if the request is valid.
    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

    // Simple check to see if the request has a content type and is a POST request
    if (exchange.getRequest().getHeaders().getContentType() == null || !exchange.getRequest().getMethod().equals(HttpMethod.POST)) {
      log.info("Request does not have a content type or is not a POST request");
      return exchange.getResponse().setComplete();
    } else {
      return DataBufferUtils.join(exchange.getRequest().getBody())
          .flatMap(dataBuffer -> {
            GatewayRequest request = requestBodyExtractor.getRequest(exchange, dataBuffer);
            ServerHttpRequest mutatedRequest = requestDecoratorFactory.getDecorator(request);
            //RouteToRequestUrlFilter writes the URI to the exchange attributes *before* any global filters run.
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mutatedRequest.getURI());
            if (request.getQueryParams() != null) {
              request.getQueryParams().clear();
            }
            log.info("Proxying request: {} {}", mutatedRequest.getMethod(), mutatedRequest.getURI());
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
          });
    }
  }
}
