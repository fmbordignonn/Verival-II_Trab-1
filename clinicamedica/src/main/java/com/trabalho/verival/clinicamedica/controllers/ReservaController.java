package com.trabalho.verival.clinicamedica.controllers;

import com.trabalho.verival.clinicamedica.entities.*;
import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaRepository reservaRepository;

    private MedicoRepository medicoRepository;

    private SalaRepository salaRepository;

    private final DateTimeFormatter datetimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");

    private final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    public ReservaController(ReservaRepository reservaRepository, MedicoRepository medicoRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.medicoRepository = medicoRepository;
        this.salaRepository = salaRepository;
    }

    @GetMapping("/list")
    public String getReservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());

        model.addAttribute("title", "Lista de reservas");

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

            LocalDateTime inicio = datetimeFormat.parseLocalDateTime(request.getDataInicio());

            LocalDateTime fim = datetimeFormat.parseLocalDateTime(request.getDataFim());

            Reserva novaReserva = new Reserva(medico.get(), sala.get(), inicio, fim);

            String reservaMessage = isReservaValid(novaReserva);

            if (reservaMessage == "sucesso") {
                reservaRepository.save(novaReserva);

                model.addAttribute(message, String.format("Reserva criada com sucesso! Medico %s na sala %s", medico.get().getNome(), sala.get().getNome()));
            } else {
                model.addAttribute(message, reservaMessage);
            }

            return "reserva/createResponse";
        } catch (Exception ex) {
            model.addAttribute(message, "Erro ao criar reserva: [" + ex.getMessage() + "]");

            return "reserva/createResponse";
        }
    }

    @GetMapping("/past")
    public String getReservasPassadas() {
        return "reserva/past";
    }

    @PostMapping("/past")
    public String getReservasPassadas(@RequestParam String dataInicio, @RequestParam String dataFim, Model model) {
        if (dataInicio.isEmpty() || dataFim.isEmpty()) {
            model.addAttribute("title", "Erro ao buscar, selecione uma data");

            return "reserva/list";
        }

        LocalDate inicio = dateFormat.parseLocalDate(dataInicio);

        LocalDate fim = dateFormat.parseLocalDate(dataFim);

        if (inicio.isAfter(LocalDate.now()) || fim.isAfter(LocalDate.now())) {
            model.addAttribute("title", "Somente é permitido buscar reservas passadas");

            return "reserva/list";
        }

        List<Reserva> reservas = reservaRepository.getReservasBetween(inicio.toDate(), fim.toDate());

        model.addAttribute("reservas", reservas);

        model.addAttribute("title", "Lista de reservas filtradas");

        return "reserva/list";
    }

    @GetMapping("/recent")
    public String getReservasRecentes(Model model) {
        model.addAttribute("reservas", reservaRepository.getReservasFuturas());

        return "reserva/list";
    }

    public static String isReservaValid (Reserva reserva) {
        if (reserva.getDataInicio().isBefore(LocalDateTime.now())) {
            return "Inicio da reserva deve ser maior que o dia atual";
        }

        if (reserva.getDataFim().isBefore(LocalDateTime.now())) {
            return "Fim da reserva deve ser maior que o dia atual";
        }

        if (reserva.getDataInicio().isAfter(reserva.getDataFim())) {
            return "Fim da reserva não pode ser antes de seu inicio";
        }

        if (!reserva.getDataInicio().toLocalDate().isEqual(reserva.getDataFim().toLocalDate())) {
            return "A reserva deve ser em um unico dia";
        }

        long diffInHours = Hours.hoursBetween(reserva.getDataInicio(), reserva.getDataFim()).getHours();

        if (diffInHours < 2) {
            return "Reservas tem um tempo minimo de 2 horas";
        }

        if (diffInHours < 3 && reserva.getSala().getTipo() == TipoSala.SALA_ALTO_RISCO.toString()) {
            return "Salas de alto risco tem um tempo minimo de reserva de 3 horas";
        }

        if (reserva.getDataInicio().getHourOfDay() < 6) {
            return "Reservas nao podem iniciar antes das 06:00";
        }

        if (reserva.getDataFim().getHourOfDay() > 22) {
            return "Reservas não podem terminar depois das 22:00";
        }

        //Optional<Reserva> salaJaOcupada = reservaRepository.checkSalaJaReservada(reserva.getSala().getId(), reserva.getDataInicio().toDate(), reserva.getDataFim().toDate());

        /*if (salaJaOcupada.isPresent()) {
            return "Já existe uma reserva para a sala no horário informado";
        }*/

        if (reserva.getMedico().getEspecialidade() == Especialidade.DERMATOLOGISTA.toString() &&
                reserva.getSala().getTipo() != TipoSala.SALA_PEQUENA.toString()) {

            return "Dermatologistas só podem reservar salas pequenas!";
        }

        if ((reserva.getMedico().getEspecialidade() == Especialidade.CIRURGIAO.toString() ||
                reserva.getMedico().getEspecialidade() == Especialidade.NEUROLOGISTA.toString()) &&
                reserva.getSala().getTipo() == TipoSala.SALA_PEQUENA.toString()) {

            return "Cirurgiões e neurologistas não podem usar salas pequenas";
        }

        return "sucesso";
    }

    @RequestMapping("/delete/{id}")
    public String deletarReserva(@PathVariable long id, Model model){
        Reserva reserva = reservaRepository.findById(id).get();


        if(reserva.getDataInicio().isBefore(LocalDateTime.now()) || reserva.getDataFim().isBefore(LocalDateTime.now())){
            model.addAttribute("message", "Não é possivel deletar reservas passadas ou em andamento");

            return "reserva/createResponse";
        }

        reservaRepository.deleteById(id);

        model.addAttribute("title", "Lista de reservas");

        return "reserva/list";
    }
}
