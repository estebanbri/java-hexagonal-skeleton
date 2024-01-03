package com.example.bfftienda.application.usecase;

import com.example.bfftienda.application.common.UseCase;
import com.example.bfftienda.application.port.in.RegistrarUsuarioPort;
import com.example.bfftienda.application.port.out.UsuarioServicePort;
import com.example.bfftienda.application.port.out.UsuarioRepositorioPort;
import com.example.bfftienda.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class AltaUsuarioUseCase implements RegistrarUsuarioPort {

    private final UsuarioRepositorioPort usuarioRepositorio;

    // Simulamos la necesidad de registrar el alta en un servicio externo ademas de guardarlo en la db
    private final UsuarioServicePort usuarioServicePort;

    @Override
    public Usuario execute(Usuario usuario) {
        usuarioServicePort.registrar(usuario);
        return usuarioRepositorio.guardar(usuario);
    }
}
