package com.empresarial.demo.crud.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "COMERCIANTE")
public class Comerciante {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Comerciante_Sequence")
    @SequenceGenerator(name = "id_Comerciante_Sequence", sequenceName = "ID_COMERCIANTE_SEQ", initialValue = 1, allocationSize = 1)
    private int id;
    private String razonSocial;
    @ManyToOne
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;
    private String telefono;
    private String correo;
    private Timestamp fechaRegistro;
    private Boolean estado;
    
    @OneToMany(mappedBy = "comerciante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Establecimiento> establecimientos;

    // Desvincular establecimientos antes de eliminar
    @PreRemove
    private void preRemove() {
        establecimientos.forEach(establecimiento -> establecimiento.setComerciante(null));
    }
    
}