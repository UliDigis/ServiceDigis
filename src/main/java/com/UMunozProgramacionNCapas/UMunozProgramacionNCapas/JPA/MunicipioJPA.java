package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "municipio")
public class MunicipioJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    private int IdMunicipio;

    @Column(name = "nombre", nullable = false, unique = false)
    private String Nombre;

    @ManyToOne
    @JoinColumn(name = "idestado", nullable = false)
    public EstadoJPA estado;

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

//    public EstadoJPA getEstado() {
//        return estado;
//    }
//
//    public void setEstado(EstadoJPA estado) {
//        this.estado = estado;
//    }

}
