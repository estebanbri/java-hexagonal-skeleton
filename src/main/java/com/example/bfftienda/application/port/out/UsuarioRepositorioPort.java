package com.example.bfftienda.application.port.out;

import com.example.bfftienda.domain.model.Usuario;

public interface UsuarioRepositorioPort {
    Usuario guardar(Usuario usuario);
    Usuario retornarPorId(Long userId);
}
