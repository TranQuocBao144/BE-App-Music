package com.music.api_gateway.repository;


import com.music.api_gateway.dto.ApiRespone;
import com.music.api_gateway.dto.reponse.IntrospectResponse;
import com.music.api_gateway.dto.request.IntrospectRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "identity-service", url = "http://localhost:8080")
public interface IdentityClient {
    @PostMapping(value = "/identity/auth/introspect", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiRespone<IntrospectResponse> introspect(@RequestBody IntrospectRequest request);
}