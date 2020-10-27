package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("medicos")
public class MedicoController {

    private MedicoRepository medicoRepository;

    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @GetMapping("listar")
    public String getMedicos(Model model){
        model.addAttribute("medicos", medicoRepository.findAll());

        return "medicos/list";
    }
}
