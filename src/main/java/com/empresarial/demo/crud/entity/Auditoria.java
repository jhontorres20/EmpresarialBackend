package com.empresarial.demo.crud.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.empresarial.demo.crud.security.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AUDITORIA")
public class Auditoria {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_auditoria_Sequence")
    @SequenceGenerator(name = "id_auditoria_Sequence", sequenceName = "ID_AUDITORIA_SEQ" , initialValue = 1, allocationSize = 1)
    private int id;
    private Timestamp fechaActualizacion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioId;

}
