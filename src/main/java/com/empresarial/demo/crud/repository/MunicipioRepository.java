package com.empresarial.demo.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empresarial.demo.crud.entity.Municipio;

import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    Optional<Municipio> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
