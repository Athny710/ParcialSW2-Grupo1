package com.example.parcialsw2.repository;


import com.example.parcialsw2.DTO.*;
import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdSelRepository extends JpaRepository<ProductoSel, Integer> {


    @Query(value="SELECT count(ps.codigo) as cantidad FROM ex1.producto_seleccionado ps, ex1.usuarios us\n" +
            "where us.rol = 'registrado'", nativeQuery=true)
    List<CantidadCompras> obtenerCantidadCompras();

    @Query(value = "SELECT DISTINCT(nombre) as caro FROM ex1.producto_seleccionado ps, ex1.producto prod\n" +
            "WHERE prod.precio = (select max(pr.precio) from ex1.producto pr)", nativeQuery = true)
    List<ProductoCaro> obtenerProductoCaro();

    @Query(value = "SELECT count(ps.idproductoseleccionado) as vendidos FROM ex1.producto_seleccionado ps\n" +
            "WHERE ps.comprado = '1'", nativeQuery = true)
    List<CantidadVendidos> obtenerCantidadVendidos();

    @Query(value = "SELECT sum(pr.precio) as facturado FROM ex1.producto_seleccionado ps, ex1.producto pr\n" +
            "WHERE pr.idproducto=ps.idproducto AND ps.comprado = '1'", nativeQuery = true)
    List<TotalFacturado> obtenerTotalFacturado();

    @Query(value = "SELECT pr.nombre AS nombre, count(pr.nombre) AS vendidos FROM ex1.producto_seleccionado ps, ex1.producto pr\n" +
            "WHERE ps.idproducto = pr.idproducto AND ps.comprado = '1'\n" +
            "GROUP BY pr.nombre\n" +
            "ORDER BY count(pr.nombre) DESC\n" +
            "LIMIT 1", nativeQuery = true)
    List<MasVendido> obtenerProductoMasVendido();

    @Query(value = "SELECT pr.nombre AS nombre, count(pr.nombre) AS vendidos FROM ex1.producto_seleccionado ps, ex1.producto pr\n" +
            "WHERE ps.idproducto = pr.idproducto AND ps.comprado = '1'\n" +
            "GROUP BY pr.nombre\n" +
            "ORDER BY count(pr.nombre) ASC\n" +
            "LIMIT 1", nativeQuery = true)
    List<MenosVendido> obtenerProductoMenosVendido();

    @Query(value = "SELECT us.nombre AS nombre,sum(pr.precio) as gasto FROM ex1.usuarios us, ex1.producto pr, ex1.producto_seleccionado ps\n" +
            "WHERE ps.idusuarios = us.idusuarios and ps.idproducto = pr.idproducto and ps.comprado = '1'", nativeQuery = true)
    List<UsuarioDerrochador> obtenerUsuarioDerrochador();


    @Query(value = " SELECT * FROM ex1.producto_seleccionado \n" +
            "where idproducto = ?1 and comprado ='0' and idusuarios = ?2 ", nativeQuery = true)
    List<ProductoSel> findByCarrito(int pro, int usu);

    @Query(value = "SELECT * FROM ex1.producto_seleccionado\n" +
            "where idusuarios = ?1 and comprado ='0'", nativeQuery = true)
    List<ProductoSel> NumeroCarrito(int u);
}
