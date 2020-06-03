package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;
import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"","/list"})
    public String index(Model model){
        model.addAttribute("lista", productoRepository.findAll());
        return "index2";

    }
    @GetMapping("/vermas")
    public String vermas(@RequestParam("id") int id, Model model, HttpSession session){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Usuario usuario = (Usuario) session.getAttribute("user");
            if (usuario == null){
                model.addAttribute("producto", opt.get());
                return "system/Detalles2";

            }
            model.addAttribute("producto", opt.get());
            return "system/Detalles";
        }else{
            return "redirect:/list";
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Producto p = opt.get();
            byte[] imagenComoBytes = p.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(p.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        }else{
            return null;
        }
    }

    @GetMapping("registrarse")
    public String registrarse(@ModelAttribute("usuario") Usuario u){
        return "system/Registrarse";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute("usuario") @Valid Usuario u, BindingResult bindingResult,
                            @RequestParam("cont2") String cont2,
                            @RequestParam("cont1") String cont1){
        if(bindingResult.hasErrors()){
            return "redirect:/registrarse";
        }else{
            if(!cont2.equals(cont1)){
                return "redirect:/registrarse";
            }else {

            }
        }

        return "";
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

    @GetMapping("/buscador")
    public String buscador(Model model, @RequestParam("search") String search){

        if(search.equalsIgnoreCase("")){
            return "redirect:/list";
        }else{
            String buscar = search + "%";
            model.addAttribute("lista",productoRepository.Buscador(buscar));
            return "index2";
        }


    }

}
