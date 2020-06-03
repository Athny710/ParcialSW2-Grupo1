package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import com.example.parcialsw2.entity.ProductoSel;
import com.example.parcialsw2.entity.Usuario;
import com.example.parcialsw2.repository.ProdSelRepository;
import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/registrado")
public class RegistradoController {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    ProdSelRepository prodSelRepository;

    @GetMapping(value = {"","/list"})
    public String paginaInicio(Model model){
        model.addAttribute("lista", productoRepository.findAll());
        return "index";
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
    @GetMapping("/carrito")
    public String añadirCarrito(@RequestParam("id") int id, HttpSession session, RedirectAttributes attr){
        Optional<Producto> opt = productoRepository.findById(id);
        if(opt.isPresent()){
            Producto p = opt.get();
            Usuario usuario = (Usuario) session.getAttribute("user");
            List<ProductoSel> seleccionado = prodSelRepository.findByProductoAndAndUsuario(p,usuario);

            if(seleccionado.isEmpty()){
                 ProductoSel productoSel= new ProductoSel();
                 productoSel.setUsuario(usuario);
                 productoSel.setProducto(p);
                 productoSel.setCantidad(1);
                attr.addFlashAttribute("msg","Se agregó correctamente");
                 prodSelRepository.save(productoSel);
            }else{
                ProductoSel pro =seleccionado.get(0);
                int cantidad = 1+ pro.getCantidad();
                if(cantidad >= 4){
                    attr.addFlashAttribute("msg1","Solo se permiten 4 unidades por producto");
                    return "redirect:/list";
                }else{
                    pro.setCantidad(cantidad);
                    attr.addFlashAttribute("msg","Se agregó correctamente");
                }

            }
            return "redirect:/list";
        }else{
            return "redirect:/list";
        }
    }

}
