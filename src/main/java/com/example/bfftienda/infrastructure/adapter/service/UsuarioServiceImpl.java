package com.example.bfftienda.infrastructure.adapter.service;

import com.example.bfftienda.application.port.out.usuario.UsuarioServicePort;
import com.example.bfftienda.domain.model.Usuario;
import com.example.bfftienda.infrastructure.adapter.gateway.usuario.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioServicePort {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioClient usuarioClient;

    @Override
    public void registrar(Usuario usuario) {
        String response = usuarioClient.registrarUsuario();
        log.info("Usuario registrado existosamente en al 3rd Party API, response: {}", response);
    }

    @Override
    public void eliminar(Usuario usuario) {
        String response = usuarioClient.eliminarUsuario();
        log.info("Usuario eliminado existosamente en al 3rd Party API, response: {}", response);
    }
}
