package com.example.parcialsw2.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    private int idusuarios;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 40, message = "Demasiados caracteres")
    @Size(min = 2, message = "Pocos caracteres")
    private String nombre;
    @NotBlank
    @Size(max = 40, message = "Demasiados caracteres")
    @Size(min = 2, message = "Pocos caracteres")
    private String apellido;
    @NotBlank
    @Size(max = 8, message = "Deben ser 8 dígitos")
    @Size(min = 8, message = "Deben ser 8 dígitos")
    private String dni;
    @NotBlank
    @Size(max=45, message = "Demasiados caracteres")
    private String correo;
    @NotBlank
    @Size(max = 10, message = "Máximo 10 caracteres")
    @Size(min = 8, message = "Mínimo 8 caracteres")
    private String contrasenha;
    @Column(nullable = false)
    private String rol;
    @Column(nullable = false)
    private int activo;


    public int getIdusuarios() {
        return idusuarios;
    }

    public void setIdusuarios(int idusuarios) {
        this.idusuarios = idusuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
