package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sala")
public class SalaController {

    private SalaRepository salaRepository;

    public SalaController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @GetMapping("/list")
    public String getSalas(Model model){
        model.addAttribute("salas", salaRepository.findAllByOrderByTipoDesc());

        return "sala/list";
    }
}
