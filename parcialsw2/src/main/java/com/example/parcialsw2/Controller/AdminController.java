package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.PaginationRepository;
import com.example.parcialsw2.repository.ProductoRepository;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    PaginationRepository paginationRepository;

    @GetMapping("/principal")
    public String index(Model model, @RequestParam(defaultValue = "0") int page){
        if(page <= 0){
            Pageable pageable = PageRequest.of(page,7);
            Page<Producto> lista1 = paginationRepository.findAll(pageable);
            int totalPages = lista1.getTotalPages();
            List<Integer> paginas = new ArrayList<>();

            for(int i=1; i<=totalPages; i++){
                paginas.add(i);
            }

            List<Producto> lista = lista1.getContent();
            model.addAttribute("page",page);
            model.addAttribute("lista",lista);
            model.addAttribute("paginas",paginas);
            return "index";
        }else{
            Pageable pageable = PageRequest.of(page -1,7);
            Page<Producto> lista1 = paginationRepository.findAll(pageable);
            int totalPages = lista1.getTotalPages();
            List<Integer> paginas = new ArrayList<>();

            for(int i=1; i<=totalPages; i++){
                paginas.add(i);
            }

            List<Producto> lista = lista1.getContent();
            model.addAttribute("page",page);
            model.addAttribute("lista",lista);
            model.addAttribute("paginas",paginas);
            return "index";
        }

    }

    @GetMapping(value = {"listaGestores","","/"})
    public String listaGestores(Model model){
        model.addAttribute("listaGestores", usuarioRepository.findByRol("gestor"));
        return "admin/listaGestores";
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
