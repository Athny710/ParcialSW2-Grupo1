package com.example.parcialsw2.Controller;

import com.example.parcialsw2.repository.ProdSelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {


    @Autowired
    ProdSelRepository prodSelRepository;

    @GetMapping(value = {"","/"})
    public String obtenerTodo(Model model){
        model.addAttribute("cantidadVendida",prodSelRepository.obtenerCantidadCompras().size());
        model.addAttribute("productocaro",prodSelRepository.obtenerProductoCaro());
        model.addAttribute("cantidadvendidos",prodSelRepository.obtenerCantidadVendidos());
        model.addAttribute("totalfacturado",prodSelRepository.obtenerTotalFacturado());
        model.addAttribute("masvendido",prodSelRepository.obtenerProductoMasVendido());
        model.addAttribute("menosvendido",prodSelRepository.obtenerProductoMenosVendido());
        model.addAttribute("masgastalon",prodSelRepository.obtenerUsuarioDerrochador());

        return "Gestor/estadisticas";
    }
}
