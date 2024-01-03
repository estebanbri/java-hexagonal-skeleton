package com.example.bfftienda.infrastructure.adapter.rest;

import com.example.bfftienda.application.port.in.AdministrarUsuarioPort;
import com.example.bfftienda.infrastructure.adapter.rest.dto.UserDto;
import com.example.bfftienda.infrastructure.adapter.rest.mapper.UsuarioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestUserAdapter {

    private final AdministrarUsuarioPort administrarUsuario;

    private final UsuarioDtoMapper usuarioMapper;

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioMapper.toDto(administrarUsuario.retornarPorId(id)), HttpStatus.OK);

    }

    @PostMapping("users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(usuarioMapper.toDto(administrarUsuario.guardar(usuarioMapper.toDomain(userDto))),
                HttpStatus.CREATED);
    }

}
