package com.empresarial.demo.crud.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ESTABLECIMIENTO")
public class Establecimiento {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Establecimiento_Sequence")
    @SequenceGenerator(name = "id_Establecimiento_Sequence", sequenceName = "ID_ESTABLECIMIENTO_SEQ", initialValue = 1, allocationSize = 1)
    private int id;
    private String nombre;
    private float precio;
    private int numeroEmpleados;
    
    @ManyToOne
    @JoinColumn(name = "comerciante_id", nullable = false)
    @JsonIgnore
    private Comerciante comerciante;

}
	