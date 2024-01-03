package com.example.bfftienda.application.port.in.usuario;

import com.example.bfftienda.domain.model.Usuario;

public interface ConsultarUsuarioPort {
    Usuario execute(Long userId);
}
