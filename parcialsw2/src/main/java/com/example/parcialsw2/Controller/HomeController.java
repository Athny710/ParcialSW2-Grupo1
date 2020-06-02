package com.example.parcialsw2.Controller;

import com.example.parcialsw2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    ProductoRepository productoRepository;

    @GetMapping(value = {"","/list"})
    public String index(Model model){
        model.addAttribute("lista", productoRepository.findAll());
        return "index";

    }
}
