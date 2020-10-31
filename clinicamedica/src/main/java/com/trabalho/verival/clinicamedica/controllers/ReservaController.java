package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/list")
    public String getReservas(Model model){
        model.addAttribute("reservas", reservaRepository.findAll());

        return "reserva/list";
    }

}
