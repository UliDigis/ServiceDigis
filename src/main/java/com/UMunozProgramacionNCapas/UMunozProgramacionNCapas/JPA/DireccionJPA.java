package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "direccion")
public class DireccionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddireccion")
    private int IdDireccion;

    @Column(name = "calle", nullable = false, unique = false)
    private String Calle;

    @Column(name = "numerointerior", nullable = false, unique = false)
    private String NumeroInterior;

    @Column(name = "numeroexterior", nullable = true, unique = false)
    private String NumeroExterior;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "idusuario", nullable = true)
    public UsuarioJPA usuario;

    @ManyToOne
    @JoinColumn(name = "idcolonia", nullable = false)
    public ColoniaJPA colonia;

    // @JsonProperty("IdUsuario")
    // public Integer getIdUsuario() {
    //     if (this.usuario == null) return null;
    //     try {
    //         return this.usuario.getIdUsuario();
    //     } catch (Exception ex) {
    //         return null;
    //     }
    // }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

}
