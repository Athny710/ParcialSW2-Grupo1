package com.example.parcialsw2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"","/inicio"})
    public String index(){
        return "index";
    }
}
