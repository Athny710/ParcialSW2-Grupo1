package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdSelRepository extends JpaRepository<ProductoSel, Integer> {

    List<ProductoSel> findByProductoAndAndUsuario(Producto pro, Usuario usu);
}
