package com.example.bfftienda.application.port.in;

import com.example.bfftienda.domain.model.Usuario;

public interface ConsultarUsuarioPort {
    Usuario execute(Long userId);
}
