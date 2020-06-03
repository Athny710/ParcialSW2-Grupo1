package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
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

import java.util.Optional;

@Controller
@RequestMapping("/registrado")
public class RegistradoController {

    @Autowired
    ProductoRepository productoRepository;

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

    @GetMapping("/vistacheck")
    public String vistacheck(){
        return "registrado/checkout";
    }
    @PostMapping("/checkout")
    public String checkout(@RequestParam("numero") String num, Model model){
        if(verificarTarjeta(num)){
            model.addAttribute("msg", "tarjeta valida");
            return "registrado/checkout";
        }else{
            model.addAttribute("msg", "tarjeta invlida");
            return "registrado/checkout";
        }

    }


    public boolean verificarTarjeta(String numero){
        char[] caracteres = numero.toCharArray();
        int[] carac2 = new int[14];
        int ultimo = Character.getNumericValue(caracteres[15]);
        int suma=0;

        for(int i=0, j=14; i<14; i++, j--){
            carac2[i] = Character.getNumericValue(caracteres[j]);
        }
        for (int i=0; i<7; i++){
            carac2[i]=carac2[i]*2;
            i++;
        }
        for (int i=0; i<14; i++){
            if(carac2[i]>=9){
                carac2[i]=carac2[i]-9;
            }
        }
        for (int i=0; i<14; i++){
            suma= suma + carac2[i];
        }
        if(suma%10==ultimo){
            return true;
        }else{
            return false;
        }
    }

}
