package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;


    private EntityManagerFactory emf;
    public void init(){
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }
    public void close(){
        emf.close();
    }
    @PostMapping("/registrar")
    public String guardar(@ModelAttribute("usuario") Usuario u){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        StoredProcedureQuery query =em.createStoredProcedureQuery("guardar");
        query.setParameter("id", u.getIdusuarios());
        query.setParameter("nombre", u.getNombre());
        query.setParameter("apellido", u.getApellido());
        query.setParameter("dni", u.getDni());
        query.setParameter("contrasenha", u.getContrasenha());
        query.setParameter("rol", u.getRol());
        query.setParameter("activo", u.getActivo());
        query.execute();
        em.getTransaction().commit();
        em.close();

        return "redirect:/list";
    }











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


    public String registrar(@ModelAttribute("usuario") @Valid Usuario u, BindingResult bindingResult,
                            @RequestParam("cont2") String cont2,
                            @RequestParam("cont1") String cont1){
        if(bindingResult.hasErrors()){
            return "redirect:/registrarse";
        }else{


            return "redirect:/list";
        }


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
