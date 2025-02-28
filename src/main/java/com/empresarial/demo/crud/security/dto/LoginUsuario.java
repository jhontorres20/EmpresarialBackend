package com.empresarial.demo.crud.security.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsuario {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    
}
