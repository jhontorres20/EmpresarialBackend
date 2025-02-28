package com.empresarial.demo.crud.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MUNICIPIO")
public class Municipio {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Municipio_Sequence")
    @SequenceGenerator(name = "id_Municipio_Sequence", sequenceName = "ID_MUNICIPIO_SEQ", initialValue = 1, allocationSize = 1)
    private int id;
    private String nombre;

}
