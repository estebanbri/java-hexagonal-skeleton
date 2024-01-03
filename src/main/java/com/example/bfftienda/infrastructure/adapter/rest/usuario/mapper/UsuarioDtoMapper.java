package com.example.bfftienda.infrastructure.adapter.rest.usuario.mapper;

import com.example.bfftienda.domain.model.Usuario;
import com.example.bfftienda.infrastructure.adapter.rest.usuario.response.UsuarioResponseDto;
import com.example.bfftienda.infrastructure.adapter.rest.usuario.request.UserRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    UsuarioResponseDto toDto(Usuario user);

    Usuario toDomain(UserRequestDto userDto);
}
