package com.example.bfftienda.application.port.out.usuario;

import com.example.bfftienda.domain.model.Usuario;

public interface UsuarioRepositorioPort {
    Usuario guardar(Usuario usuario);
    Usuario retornarPorId(Long userId);
}
