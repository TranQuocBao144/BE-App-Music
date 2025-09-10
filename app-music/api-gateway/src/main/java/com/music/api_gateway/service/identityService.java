package com.music.api_gateway.service;



import com.music.api_gateway.dto.ApiRespone;
import com.music.api_gateway.dto.reponse.IntrospectResponse;
import com.music.api_gateway.dto.request.IntrospectRequest;
import com.music.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class identityService {
    IdentityClient identityClient;
    public Mono<ApiRespone<IntrospectResponse>> asyncIntrospect(String token) {
        return Mono.fromCallable(() -> identityClient.introspect(
                        IntrospectRequest.builder().token(token).build()))
                .subscribeOn(Schedulers.boundedElastic()); // Đưa vào thread pool khác
    }
}
