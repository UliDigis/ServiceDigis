package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pais")
public class PaisJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpais")
    private int IdPais;
    
    @Column(name = "nombre", nullable = false, unique = false)
    private String Nombre;

    public int getIdPais() {
        return IdPais;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    
    
    
}
