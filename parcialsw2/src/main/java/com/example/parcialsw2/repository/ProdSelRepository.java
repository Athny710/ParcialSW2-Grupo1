package com.example.parcialsw2.repository;


import com.example.parcialsw2.DTO.*;
import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdSelRepository extends JpaRepository<ProductoSel, Integer> {


    @Query(value="SELECT count(ps.idproductoseleccionado) as cantidad FROM ex1.producto_seleccionado ps, ex1.usuarios us\n" +
            "where us.rol = 'registrado'\n" +
            "group by ps.codigo", nativeQuery=true)
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

    @Query(value = "SELECT us.nombre as nombre, sum(ps.preciototal) as gasto FROM ex1.producto_seleccionado ps, ex1.usuarios us\n" +
            "where ps.comprado = 1 and ps.idusuarios = us.idusuarios\n" +
            "group by ps.idusuarios\n" +
            "order by sum(ps.preciototal) desc\n" +
            "limit 1", nativeQuery = true)
    List<UsuarioDerrochador> obtenerUsuarioDerrochador();


    @Query(value = " SELECT * FROM ex1.producto_seleccionado \n" +
            "where idproducto = ?1 and comprado ='0' and idusuarios = ?2 ", nativeQuery = true)
    List<ProductoSel> findByCarrito(int pro, int usu);

    @Query(value = "SELECT * FROM ex1.producto_seleccionado\n" +
            "where idusuarios = ?1 and comprado ='0'", nativeQuery = true)
    List<ProductoSel> NumeroCarrito(int u);

    @Query(value = "SELECT * FROM ex1.producto_seleccionado ps\n" +
            "WHERE ps.idusuarios = ?1 AND ps.comprado = '1'\n" +
            "GROUP BY ps.codigo",nativeQuery = true)
    List<ProductoSel> obtenerCodigos(int id);

    @Query(value = "SELECT pr.nombre AS nombre, ps.codigo AS codigo, ps.cantidad AS cantidad, pr.precio AS precio, ps.preciototal AS pretoto, ps.fecha AS fechita FROM ex1.producto_seleccionado ps, ex1.producto pr\n" +
            "WHERE ps.codigo=?1 AND ps.idproducto = pr.idproducto", nativeQuery = true)
    List<ProductosxCodigo> obtenerProductosXCodigo(String code);

    @Query(value = "SELECT DISTINCT(ps.codigo) as cantcodi FROM ex1.producto_seleccionado ps", nativeQuery = true)
    List<CodigosTotales> obtenerCantidadDeCodigos();


    List<ProductoSel> findByProducto(Producto producto);

}
