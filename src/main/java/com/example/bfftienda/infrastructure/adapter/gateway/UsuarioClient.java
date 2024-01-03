package com.example.bfftienda.infrastructure.adapter.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-usuario", url = "https://httpbin.org")
public interface UsuarioClient {

    @PostMapping(path = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    String registrarUsuario();

    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    String eliminarUsuario();
}

