package com.music.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.api_gateway.dto.ApiRespone;
import com.music.api_gateway.dto.request.IntrospectRequest;
import com.music.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthencationFilter implements GlobalFilter, Ordered {
    private final ObjectProvider<IdentityClient> identityClientProvider;
    private final ObjectMapper objectMapper;


    @NonFinal
    String[] publicEndpoints={"/identity/auth/.*","/identity/users/registration"};



    public AuthencationFilter(ObjectProvider<IdentityClient> identityClientProvider,
                              ObjectMapper objectMapper) {
        this.identityClientProvider = identityClientProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthencationFilter......");
        if (isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }
        // 1. Lấy token từ header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader) || !authHeader.get(0).startsWith("Bearer ")) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.get(0).substring(7).trim();
        log.info("Token: {}", token);

        // 2. Lấy IdentityClient muộn
        IdentityClient identityClient = identityClientProvider.getIfAvailable();
        if (identityClient == null) {
            log.error("IdentityClient bean chưa sẵn sàng");
            return unauthenticated(exchange.getResponse());
        }

        // 3. Gọi Feign trong thread pool khác để tránh blocking WebFlux
        return Mono.fromCallable(() -> identityClient.introspect(new IntrospectRequest(token)))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
                .flatMap(resp -> {
                    var result = resp.getResult();
                    if (result != null && Boolean.TRUE.equals(result.isValid())) {
                        return chain.filter(exchange);
                    } else {
                        return unauthenticated(exchange.getResponse());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Lỗi xác thực token:", e);
                    return unauthenticated(exchange.getResponse());
                });
    }



    @Override
    public int getOrder() {
        return -1;
    }
    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiRespone<?> apiResponse = ApiRespone.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }


    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(s));
    }
}
