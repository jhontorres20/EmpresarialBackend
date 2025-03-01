package com.empresarial.demo.crud.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import com.empresarial.demo.crud.dto.Mensaje;
import com.empresarial.demo.crud.security.dto.JwtDto;
import com.empresarial.demo.crud.security.dto.LoginUsuario;
import com.empresarial.demo.crud.security.entity.Usuario;
import com.empresarial.demo.crud.security.jwt.JwtProvider;
import com.empresarial.demo.crud.security.repository.UsuarioRepository;

import java.text.ParseException;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtProvider jwtProvider;
    
    private final String secretKey = "pTXgQ42qz2HOqF2rgLUlnrhmSjM8HTOX";
    private final String initVector = "1234567890123456";

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    public boolean existsByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
        usuarioRepository.flush();
    }
    
    public ResponseEntity<JwtDto> login(LoginUsuario loginUsuario, BindingResult bindingResult) {
    	String decryptedPassword = "";
    	try {
			decryptedPassword = decrypt(loginUsuario.getPassword());
		} catch (Exception e) {
			System.out.println("Error al autenticar " + e.getMessage());
			return new ResponseEntity("Error al autenticar", HttpStatus.BAD_REQUEST);
		}
    	if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(), decryptedPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
    
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return new ResponseEntity(jwt, HttpStatus.OK);
    }
    
    private String decrypt(String encryptedPassword) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        byte[] decodedPassword = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedPassword);
        return new String(decryptedBytes, "UTF-8");
    }
}
