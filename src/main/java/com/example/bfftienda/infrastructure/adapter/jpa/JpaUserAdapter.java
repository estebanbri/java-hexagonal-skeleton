package com.example.bfftienda.infrastructure.adapter.jpa;

import com.example.bfftienda.application.port.out.usuario.UsuarioRepositorioPort;
import com.example.bfftienda.domain.model.Usuario;
import com.example.bfftienda.infrastructure.adapter.jpa.mapper.UsuarioEntityMapper;
import com.example.bfftienda.infrastructure.adapter.jpa.repository.SpringJpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserAdapter implements UsuarioRepositorioPort {

    private final SpringJpaUsuarioRepository springJpaUsuarioRepository;

    private final UsuarioEntityMapper usuarioMapper;

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioMapper.toDomain(springJpaUsuarioRepository.save(usuarioMapper.toEntity(usuario)));
    }

    @Override
    public Usuario retornarPorId(Long userId) {
        return usuarioMapper.toDomain(springJpaUsuarioRepository.findById(userId).orElseThrow());
    }
}
