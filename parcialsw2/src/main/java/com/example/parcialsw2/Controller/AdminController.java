package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("crear")
    public String nuevoGestor(@ModelAttribute("usuario") Usuario usuario){
        return "admin/formGestor";
    }

    @PostMapping("guardar")
    public String guardarPost(@ModelAttribute("usuario") @Valid Usuario usuario,
                              BindingResult bindingResult,
                              Model model, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            return "admin/formGestor";
        }else{

            usuario.setRol("gestor");
            usuario.setActivo(1);
            usuario.setContrasenha("123");
            attr.addFlashAttribute("msg", "Gestor creado exitosamente!");
            usuarioRepository.save(usuario);
            return "redirect:/admin/listaGestores";
        }
    }
}
