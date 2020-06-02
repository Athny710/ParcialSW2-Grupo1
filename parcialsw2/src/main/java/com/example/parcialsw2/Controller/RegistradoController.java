package com.example.parcialsw2.Controller;

import com.example.parcialsw2.entity.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registrado")
public class RegistradoController {

    @GetMapping(value = {"","/list"})
    public String paginaInicio(){
        return "index";
    }


}
