package com.ProjetoP2.Yago_42386.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaInicialController {

    @GetMapping("/")
    public String inicio() { return "index"; }

}
