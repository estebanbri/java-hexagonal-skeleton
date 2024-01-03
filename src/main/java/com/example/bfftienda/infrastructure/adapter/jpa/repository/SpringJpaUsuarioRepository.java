package com.example.bfftienda.infrastructure.adapter.jpa.repository;

import com.example.bfftienda.infrastructure.adapter.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringJpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

}