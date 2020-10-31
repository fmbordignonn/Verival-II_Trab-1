package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.entities.Medico;
import com.trabalho.verival.clinicamedica.entities.Reserva;
import com.trabalho.verival.clinicamedica.entities.ReservaRequest;
import com.trabalho.verival.clinicamedica.entities.Sala;
import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaRepository reservaRepository;

    private MedicoRepository medicoRepository;

    private SalaRepository salaRepository;

    public ReservaController(ReservaRepository reservaRepository, MedicoRepository medicoRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.medicoRepository = medicoRepository;
        this.salaRepository = salaRepository;
    }

    @GetMapping("/list")
    public String getReservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());

        return "reserva/list";
    }

    @GetMapping("/create")
    public String createReserva(Model model) {
        model.addAttribute("request", new ReservaRequest());

        model.addAttribute("medicos", medicoRepository.findAll());

        model.addAttribute("salas", salaRepository.findAll());

        return "reserva/create";
    }

    @PostMapping("/create")
    public String createReserva(@ModelAttribute ReservaRequest request, Model model) {

        Optional<Medico> medico = medicoRepository.findById(request.getIdMedico());

        if (!medico.isPresent()) {
            model.addAttribute("message", "Medico nao encontrado");
            return "reserva/createResponse";
        }

        Optional<Sala> sala = salaRepository.findById(request.getIdSala());

        if (!sala.isPresent()) {
            model.addAttribute("message", "Sala nao encontrado");
            return "reserva/createResponse";
        }

        Reserva reserva = new Reserva(medico.get(), sala.get(), new Date(), new Date());

        reservaRepository.save(reserva);

        model.addAttribute("message", String.format("Reserva criada com sucesso! Medico %s na sala %s", medico.get().getNome(), sala.get().getNome()));

        return "reserva/createResponse";
    }
}
