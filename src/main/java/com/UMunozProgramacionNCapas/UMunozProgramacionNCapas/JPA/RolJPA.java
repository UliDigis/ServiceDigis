package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rol")
public class RolJPA {

    @Id
    @GeneratedValue
    @Column(name = "idrol")
    private int IdRol;

    @Column(name = "nombre", nullable = false, unique = false)
    private String Nombre;

    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    
}
