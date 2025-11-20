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
@Table(name = "colonia")
public class ColoniaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcolonia")
    private int IdColonia;
    
    @Column(name = "nombre", nullable = false, unique = false)
    private String Nombre;
    
    @Column(name = "codigopostal", nullable = false, unique = true)
    private String CodigoPostal;
    
    @ManyToOne
    @JoinColumn(name = "idmunicipio", nullable = false)
    private MunicipioJPA municipio;

    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

//    public MunicipioJPA getMunicipio() {
//        return municipio;
//    }
//
//    public void setMunicipio(MunicipioJPA municipio) {
//        this.municipio = municipio;
//    }
    

    
}
