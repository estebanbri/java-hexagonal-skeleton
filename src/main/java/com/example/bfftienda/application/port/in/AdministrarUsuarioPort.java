package com.example.bfftienda.application.port.in;


import com.example.bfftienda.domain.model.Usuario;

public interface AdministrarUsuarioPort {
    Usuario guardar(Usuario user);
    Usuario retornarPorId(Long userId);
}