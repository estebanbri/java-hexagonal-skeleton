package com.example.bfftienda.infrastructure.adapter.rest.mapper;

import com.example.bfftienda.domain.model.Usuario;
import com.example.bfftienda.infrastructure.adapter.rest.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    UserDto toDto(Usuario user);

    Usuario toDomain(UserDto userDto);
}
