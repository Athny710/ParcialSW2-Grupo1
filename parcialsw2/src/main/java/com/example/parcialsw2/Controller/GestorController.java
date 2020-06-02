package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.ProductoRepository;
import com.example.parcialsw2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/gestor")
public class GestorController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProductoRepository productoRepository;

    @GetMapping(value = {"","/list"})
    public String paginaInicio(){
        return "index";
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

            return "redirect:/user/list";
        }
    }


}
