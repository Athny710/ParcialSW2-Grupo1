package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query(value = "SELECT * FROM ex1.producto\n" +
            "WHERE LOWER(nombre) LIKE ?1 or nombre LIKE ?1", nativeQuery = true)
    List<Producto> Buscador(String search);

}
