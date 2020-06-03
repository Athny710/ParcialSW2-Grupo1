package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaginationRepository extends PagingAndSortingRepository<Producto, Integer> {
}
