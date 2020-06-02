package com.example.parcialsw2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/loginForm")
    public String loginForm(){
        return"iniciarSesion";
    }

    @GetMapping("/redirectByRole")
    public String redirigir(Authentication auth, HttpSession session){
        String rol = "";

        for(GrantedAuthority role : auth.getAuthorities()){
            rol = role.getAuthority();
        }
        if(rol.equalsIgnoreCase("administrador")){
            Usuarios usuarioLogueado = usuarioRepository.findByCorreo(auth.getName());
            session.setAttribute("user",usuarioLogueado);
            return "redirect:/admin";
        }else if (rol.equalsIgnoreCase("sede")){
            Usuarios usuarioLogueado = usuarioRepository.findByCorreo(auth.getName());
            session.setAttribute("user",usuarioLogueado);
            return "redirect:/sede";
        }else if (rol.equalsIgnoreCase("gestor")){
            Usuarios usuarioLogueado = usuarioRepository.findByCorreo(auth.getName());
            session.setAttribute("user",usuarioLogueado);
            return "redirect:/gestor";
        }else {
            return "index";
        }
    }
}
