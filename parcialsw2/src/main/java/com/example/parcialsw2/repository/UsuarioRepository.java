package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByCorreo(String correo);


    public List<Usuario> findByRol(String rol);
    Usuario findByIdusuarios(String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ex1`.`usuarios` SET `contrasenha` = ?1 WHERE (`idusuarios` = ?2);", nativeQuery = true)
    public void guardarContra(String contra, int id);

}
