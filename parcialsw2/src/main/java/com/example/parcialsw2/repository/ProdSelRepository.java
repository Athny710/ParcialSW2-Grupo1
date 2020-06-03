package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdSelRepository extends JpaRepository<ProductoSel, Integer> {


    @Query(value = " SELECT * FROM ex1.producto_seleccionado \n" +
            "where idproducto = ?1 and comprado ='0' and idusuarios = ?2 ", nativeQuery = true)
    List<ProductoSel> findByCarrito(int pro, int usu);

    @Query(value = "SELECT * FROM ex1.producto_seleccionado\n" +
            "where idusuarios = ?1 and comprado ='0'", nativeQuery = true)
    List<ProductoSel> NumeroCarrito(int u);
}
