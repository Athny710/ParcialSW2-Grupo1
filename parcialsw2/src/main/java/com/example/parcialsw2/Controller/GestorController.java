package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.PaginationRepository;
import com.example.parcialsw2.repository.ProdSelRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestor")
public class GestorController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    PaginationRepository paginationRepository;
    @Autowired
    ProdSelRepository prodSelRepository;

    @GetMapping(value = {"","/list"})
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

    @GetMapping("/nuevo")
    public String crear(@ModelAttribute("producto") Producto producto){

        return "Gestor/form";
    }

    @GetMapping("editar")
    public String editarProducto(@ModelAttribute("producto") Producto producto, Model model, @RequestParam("id") int id) {
        Optional<Producto> pro1 = productoRepository.findById(id);
        if (pro1.isPresent()) {
            producto = pro1.get();
            model.addAttribute("producto", producto);
            return "Gestor/form";
        } else {
            return "redirect:/gestor/list";
        }
    }

    @PostMapping("guardar")
    public String guardarEmployee(@ModelAttribute("producto") @Valid Producto producto, BindingResult bindingResult, RedirectAttributes attr,
                                  @RequestParam("archivo") MultipartFile file, Model model) {

        String fileName = file.getOriginalFilename();

        if (bindingResult.hasErrors() || file.isEmpty() ){
            model.addAttribute("msg","Debe subir un Archivo");
            return "Gestor/form";
        } else{
            if (fileName.contains("..")){
                model.addAttribute("msg","No se permiten '..' en el archivo");
                return "Gestor/form";
            }else {
                if (producto.getIdproducto() == 0) {
                    attr.addFlashAttribute("msg", "Producto creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "Producto actualizado exitosamente");
                }

                try {

                    producto.setFoto(file.getBytes());
                    producto.setFotonombre(fileName);
                    producto.setFotocontenttype(file.getContentType());
                    productoRepository.save(producto);

                }catch (IOException e){
                    e.printStackTrace();
                    model.addAttribute("msg", "Ocurrió un error al subir el archivo");
                    return "Gestor/form";
                }

            }

            return "redirect:/list";
        }
    }


    @GetMapping("/eliminar")
    public String eliminar(@RequestParam("id") int id, RedirectAttributes attr){

        Optional<Producto> otpro = productoRepository.findById(id);

        if(otpro.isPresent()){
            Producto pro = otpro.get();
            List<ProductoSel> lista = prodSelRepository.findByProducto(pro);

                if(lista.isEmpty()){
                    attr.addFlashAttribute("msg", "Borrado exitosamente");
                    productoRepository.deleteById(id);
                    return "redirect:/list";
                }else{
                    attr.addFlashAttribute("msg1", "No puede ser Borrado");
                    return "redirect:/list";
                }

        }else {
            attr.addFlashAttribute("msg1", "El producto no existe");
            return "redirect:/list";
        }



    }
}
