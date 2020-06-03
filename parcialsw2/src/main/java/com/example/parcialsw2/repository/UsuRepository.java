package com.example.parcialsw2.repository;

import com.example.parcialsw2.entity.Usuario;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

public interface UsuRepository extends CrudRepository<Usuario,Integer> {

    @Procedure
    void guardarUsuario(int id, String nombre, String Apellido, String dni, String correo, String pass, String rol, int activo);
}
