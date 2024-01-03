package com.example.bfftienda.application.usecase.usuario;

import com.example.bfftienda.application.common.UseCase;
import com.example.bfftienda.application.port.in.usuario.ConsultarUsuarioPort;
import com.example.bfftienda.application.port.out.usuario.UsuarioRepositorioPort;
import com.example.bfftienda.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class ObtenerUsuarioUseCase implements ConsultarUsuarioPort {

    private final UsuarioRepositorioPort usuarioRepositorio;

    @Override
    public Usuario execute(Long usuarioId) {
        return usuarioRepositorio.retornarPorId(usuarioId);
    }
}
