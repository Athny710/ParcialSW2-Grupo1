package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {


}
