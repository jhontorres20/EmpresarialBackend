package com.empresarial.demo.crud.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComercianteEstadisticaDto {
	
	private String nombre;
    private String municipio;
    private String telefono;
    private String correoElectronico;
    private String fechaRegistro;
    private String estado;
    private Integer cantidadEstablecimientos;
    private Double totalIngresos;
    private Integer cantidadEmpleados;

    public ComercianteEstadisticaDto(Object[] datos) {
        this.nombre = (String) datos[1];
        this.municipio = (String) datos[2];
        this.telefono = (String) datos[3];
        this.correoElectronico = (String) datos[4];
        this.fechaRegistro = (String) datos[5];
        this.estado = (String) datos[6];
        this.cantidadEstablecimientos = ((Number) datos[7]).intValue();
        this.totalIngresos = ((Number) datos[8]).doubleValue();
        this.cantidadEmpleados = ((Number) datos[9]).intValue();
    }


}
