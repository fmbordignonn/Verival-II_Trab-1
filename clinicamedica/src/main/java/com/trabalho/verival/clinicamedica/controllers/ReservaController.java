package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.entities.*;
import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaRepository reservaRepository;

    private MedicoRepository medicoRepository;

    private SalaRepository salaRepository;

    @Autowired
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
    public String createReserva(@ModelAttribute ReservaRequest request, Model model) throws Exception {
        String message = "message";

        try {
            Optional<Medico> medico = medicoRepository.findById(request.getIdMedico());

            if (!medico.isPresent()) {
                model.addAttribute(message, "Medico nao encontrado");
                return "reserva/createResponse";
            }

            Optional<Sala> sala = salaRepository.findById(request.getIdSala());

            if (!sala.isPresent()) {
                model.addAttribute(message, "Sala nao encontrada");
                return "reserva/createResponse";
            }

            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");

            LocalDateTime inicio = format.parseLocalDateTime(request.getDataInicio());

            LocalDateTime fim = format.parseLocalDateTime(request.getDataFim());

            if (inicio.isAfter(fim)) {
                model.addAttribute(message, "Fim da reserva não pode ser antes de seu inicio");

                return "reserva/createResponse";
            }

            //ver se leva em conta o dia
            long diffInHours = Hours.hoursBetween(inicio, fim).getHours();

            //sala de alto risco tem q ser no minimo 3 horas
            if (diffInHours < 2) {
                model.addAttribute(message, "Reservas tem um tempo minimo de duas horas");

                return "reserva/createResponse";
            }

            if(inicio.getHourOfDay() < 6){
                model.addAttribute(message, "Reservas nao podem iniciar antes das 06:00");

                return "reserva/createResponse";
            }

            if(fim.getHourOfDay() > 22){
                model.addAttribute(message, "Reservas não podem terminar depois das 22:00");

                return "reserva/createResponse";
            }

            if (medico.get().getEspecialidade() == Especialidade.DERMATOLOGISTA.toString() &&
                    sala.get().getTipo() != TipoSala.SALA_PEQUENA.toString()) {

                model.addAttribute(message, "Dermatologistas só podem reservar salas pequenas!");

                return "reserva/createResponse";
            }

            if ((medico.get().getEspecialidade() == Especialidade.CIRURGIAO.toString() ||
                    medico.get().getEspecialidade() == Especialidade.NEUROLOGISTA.toString()) &&
                    sala.get().getTipo() == TipoSala.SALA_PEQUENA.toString()) {

                model.addAttribute(message, "Cirurgiões e neurologistas não podem usar salas pequenas");

                return "reserva/createResponse";
            }

            Reserva reserva = new Reserva(medico.get(), sala.get(), inicio, fim);

            reservaRepository.save(reserva);

            model.addAttribute(message, String.format("Reserva criada com sucesso! Medico %s na sala %s", medico.get().getNome(), sala.get().getNome()));

            return "reserva/createResponse";
        } catch (Exception ex) {
            model.addAttribute(message, "Erro ao criar reserva: [" + ex.getMessage() + "]");

            return "reserva/createResponse";
        }
    }
}
