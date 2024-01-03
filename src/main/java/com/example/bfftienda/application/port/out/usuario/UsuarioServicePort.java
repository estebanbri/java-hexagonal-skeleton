package com.example.bfftienda.application.port.out.usuario;

import com.example.bfftienda.domain.model.Usuario;

// Simulamos que tenemos ademas de almaenar el usuario debemos que registrar al usuario en una api externa de la compañia
public interface UsuarioServicePort {
    void registrar(Usuario usuario);
    void eliminar(Usuario usuario);
}
