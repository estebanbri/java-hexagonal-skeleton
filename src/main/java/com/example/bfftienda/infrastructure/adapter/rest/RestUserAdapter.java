package com.example.bfftienda.infrastructure.adapter.rest;

import com.example.bfftienda.application.port.in.ConsultarUsuarioPort;
import com.example.bfftienda.application.port.in.RegistrarUsuarioPort;
import com.example.bfftienda.infrastructure.adapter.rest.dto.UserDto;
import com.example.bfftienda.infrastructure.adapter.rest.mapper.UsuarioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestUserAdapter {

    // Tal como menciono en el readme los puertos inbound no son necesarios crearlos por ende aqui podria usar directamente los use-cases
    // Pero esta hecho asi a proposito para dejar bien en claro que la comunicacion de los adaptadores es atraves de los ports de la aplicación.
    private final RegistrarUsuarioPort registrarUsuarioPort;
    private final ConsultarUsuarioPort consultarUsuarioPort;

    private final UsuarioDtoMapper usuarioMapper;

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioMapper.toDto(consultarUsuarioPort.execute(id)), HttpStatus.OK);

    }

    @PostMapping("users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(usuarioMapper.toDto(registrarUsuarioPort.execute(usuarioMapper.toDomain(userDto))),
                HttpStatus.CREATED);
    }

}
