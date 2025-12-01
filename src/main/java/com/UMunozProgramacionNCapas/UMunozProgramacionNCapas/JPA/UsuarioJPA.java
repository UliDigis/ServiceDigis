package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "usuario")
public class UsuarioJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private int IdUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String userName;

    @Column(name = "nombre", nullable = false, unique = false, length = 50)
    private String Nombre;

    @Column(name = "apellidopaterno", nullable = false, unique = false, length = 50)
    private String ApellidoPaterno;

    @Column(name = "apellidomaterno", nullable = true, unique = false, length = 50)
    private String ApellidoMaterno;

    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String Email;

    @Column(name = "password", nullable = false, unique = false)
    private String Password;
    @Column(name = "fechanacimiento", nullable = false, unique = false)
    private LocalDate FechaNacimiento;

    @Column(name = "sexo", nullable = false, unique = false, length = 1)
    private char Sexo;

    @Column(name = "telefono", nullable = false, unique = false, length = 20)
    private String Telefono;

    @Column(name = "celular", nullable = true, unique = false, length = 20)
    private String Celular;

    @Column(name = "CURP", nullable = false, unique = true, length = 30)
    private String CURP;
    
    @Lob
    @Column(name = "imagen", nullable = true, unique = false)
    private String Imagen;

    @ManyToOne
    @JoinColumn(name = "idrol", nullable = false)
    public RolJPA rol;
    
    @Column(name = "status", nullable = false)
    private boolean Status;
    
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DireccionJPA> Direcciones;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public LocalDate getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public char getSexo() {
        return Sexo;
    }

    public void setSexo(char Sexo) {
        this.Sexo = Sexo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }
}
