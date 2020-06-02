package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"listaGestores","","/"})
    public String listaGestores(Model model){
        model.addAttribute("listaGestores", usuarioRepository.findByRol("gestor"));
        return "admin/listaGestores";
    }

    @GetMapping("nuevo")
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
            attr.addFlashAttribute("msg", "Gestor "+(usuario.getIdusuarios()==0 ? "creado " : "actualizado ")+"exitosamente");
            usuarioRepository.save(usuario);
            return "redirect:/admin/listaGestores";
        }
    }

    @GetMapping("editar")
    public String editarEmployee(@ModelAttribute("usuario")  Usuario usuario,Model model, @RequestParam("id") int id) {
        Optional<Usuario> user1 = usuarioRepository.findById(id);
        if (user1.isPresent()) {
            usuario = user1.get();
            model.addAttribute("usuario", usuario);
            return "admin/editGestor";
        } else {
            return "redirect:/admin/listaGestores";
        }
    }
}
