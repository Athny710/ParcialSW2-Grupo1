package com.example.parcialsw2.entity;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "producto_seleccionado")
public class ProductoSel {

    @Id
    private int idproductoseleccionado;
    private int cantidad;
    @ManyToOne
    @JoinColumn(name = "idproducto")
    private Producto producto;
    @ManyToOne
    @JoinColumn(name = "idusuarios")
    private Usuario usuario;
    private String codigo;
    private Integer comprado;
    private double preciototal;
    private String fecha;


    public int getIdproductoseleccionado() {
        return idproductoseleccionado;
    }

    public void setIdproductoseleccionado(int idproductoseleccionado) {
        this.idproductoseleccionado = idproductoseleccionado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getComprado() {
        return comprado;
    }

    public void setComprado(Integer comprado) {
        this.comprado = comprado;
    }



    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(double preciototal) {
        this.preciototal = preciototal;
    }
}
