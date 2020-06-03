package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByCorreo(String correo);
    public List<Usuario> findByRol(String rol);


}
