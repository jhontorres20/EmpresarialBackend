package com.empresarial.demo.crud.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.empresarial.demo.crud.security.enums.RolNombre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ROL")
public class Rol {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Role_Sequence")
    @SequenceGenerator(name = "id_Role_Sequence", sequenceName = "ID_ROLE_SEQ", initialValue = 1, allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;
    
}
