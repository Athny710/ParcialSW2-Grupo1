package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.ProdSelRepository;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProdSelRepository prodSelRepository;

    @GetMapping("/loginForm")
    public String loginForm(){
        return"iniciarSesion";
    }

    @GetMapping("/redirectByRole")
    public String redirigir(Authentication auth, HttpSession session){
        String rol = "";
        Usuario usuarioLogueado = usuarioRepository.findByCorreo(auth.getName());
        List<ProductoSel> paraElNumero = prodSelRepository.NumeroCarrito(usuarioLogueado.getIdusuarios());
        int numeroCarrito = 0;
        for(ProductoSel recorrer : paraElNumero ){
            numeroCarrito = numeroCarrito + recorrer.getCantidad();
        }

        for(GrantedAuthority role : auth.getAuthorities()){
            rol = role.getAuthority();
        }
        if(rol.equalsIgnoreCase("admin")){
            session.setAttribute("numeroCarrito",numeroCarrito);
            session.setAttribute("user",usuarioLogueado);

            return "redirect:/admin";
        }else if (rol.equalsIgnoreCase("registrado")){
            session.setAttribute("numeroCarrito",numeroCarrito);
            session.setAttribute("user",usuarioLogueado);

            return "redirect:/registrado";
        }else if (rol.equalsIgnoreCase("gestor")){
            session.setAttribute("numeroCarrito",numeroCarrito);
            session.setAttribute("user",usuarioLogueado);

            return "redirect:/gestor";
        }else {
            return "index";
        }
    }
}
