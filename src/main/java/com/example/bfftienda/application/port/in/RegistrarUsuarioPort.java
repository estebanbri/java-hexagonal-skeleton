package com.example.bfftienda.application.port.in;


import com.example.bfftienda.domain.model.Usuario;

public interface RegistrarUsuarioPort {
    Usuario execute(Usuario user);
}