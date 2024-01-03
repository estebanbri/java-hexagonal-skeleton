package com.example.bfftienda.infrastructure.adapter.jpa.mapper;

import com.example.bfftienda.domain.model.Usuario;
import com.example.bfftienda.infrastructure.adapter.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {

    UsuarioEntity toEntity(Usuario user);

    Usuario toDomain(UsuarioEntity userEntity);
}