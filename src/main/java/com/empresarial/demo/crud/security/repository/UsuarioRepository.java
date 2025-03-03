package com.empresarial.demo.crud.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresarial.demo.crud.security.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
	Optional<Usuario> findByNombreUsuario(String nombreUsuario);
	
	Optional<Usuario> findByEmail(String email);
    
	boolean existsByNombreUsuario(String nombreUsuario);
    
	boolean existsByEmail(String email);

}
