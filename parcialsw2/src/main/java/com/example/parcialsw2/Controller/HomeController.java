package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class HomeController {

    @GetMapping(value = {"","/inicio"})
    public String index(){
        return "index";
    }

    @GetMapping("registrarse")
    public String registrarse(){

        return "system/Registrarse";
    }
    @GetMapping("recuperar")
    public String recuperarContra(){

        return "system/RecuperarCont";
    }

    @PostMapping("/processLogin")
    public String registrarCuenta(@RequestParam("nombre") String nombre,
                              @RequestParam("apellido") String apellido,
                              @RequestParam("dni") String dni,
                              @RequestParam("correo") String correo,
                              @RequestParam("contraseña") String contrasenha,
                              @RequestParam("cont2") String confirmar,
                              Model model, RedirectAttributes attr,
                              HttpSession session){
        if(!"".equals(contrasenha) && !"".equals(confirmar)){
            if(contrasenha.equals(confirmar)){
                attr.addFlashAttribute("msg", "Contraseña actualizada.");
                Usuario usuarioLog=(Usuario) session.getAttribute("user");
                usuarioLog.setContrasenha(contrasenha);
                session.setAttribute("user", usuarioLog);
                return "redirect:/admin";
            }else{
                attr.addFlashAttribute("msg", "Las contraseñas no coinciden");
                return "redirect:/registrarse";
            }
        }else{
            attr.addFlashAttribute("msg", "No puede haber campos vacíos.");
            return "redirect:/registrarse";
        }
    }



}
