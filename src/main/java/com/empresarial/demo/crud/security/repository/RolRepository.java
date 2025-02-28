package com.empresarial.demo.crud.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empresarial.demo.crud.security.entity.Rol;
import com.empresarial.demo.crud.security.enums.RolNombre;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
	Optional<Rol> findByRolNombre(RolNombre rolNombre);
}
