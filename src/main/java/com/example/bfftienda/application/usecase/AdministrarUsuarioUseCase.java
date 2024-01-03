package com.example.bfftienda.application.usecase;

import com.example.bfftienda.application.common.UseCase;
import com.example.bfftienda.application.port.in.AdministrarUsuarioPort;
import com.example.bfftienda.application.port.out.UsuarioRepositorioPort;
import com.example.bfftienda.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class AdministrarUsuarioUseCase implements AdministrarUsuarioPort {

    private final UsuarioRepositorioPort usuarioRepositorio;

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.guardar(usuario);
    }

    @Override
    public Usuario retornarPorId(Long userId) {
        return usuarioRepositorio.retornarPorId(userId);
    }
}
