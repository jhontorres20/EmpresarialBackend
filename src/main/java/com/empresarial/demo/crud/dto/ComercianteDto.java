package com.empresarial.demo.crud.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComercianteDto {
  
    @NotBlank
    private String razonSocial;
    private Integer municipio;
    @NotBlank
    private String telefono;    
    private String correo;
    private Timestamp fechaRegistro;
    private Boolean estado;


}
