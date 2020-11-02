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

            if(inicio.isBefore(LocalDateTime.now())){
                model.addAttribute(message, "Inicio da reserva deve ser maior que o dia atual");

                return "reserva/createResponse";
            }

            if(fim.isBefore(LocalDateTime.now())){
                model.addAttribute(message, "Fim da reserva deve ser maior que o dia atual");

                return "reserva/createResponse";
            }

            if (inicio.isAfter(fim)) {
                model.addAttribute(message, "Fim da reserva não pode ser antes de seu inicio");

                return "reserva/createResponse";
            }

            if (!datetimeFormat.parseLocalDate(request.getDataInicio()).isEqual(datetimeFormat.parseLocalDate(request.getDataFim()))) {
                model.addAttribute(message, "A reserva deve ser em um unico dia");

                return "reserva/createResponse";
            }

            //considerar minutos
            long diffInHours = Hours.hoursBetween(inicio, fim).getHours();

            if (diffInHours < 2) {
                model.addAttribute(message, "Reservas tem um tempo minimo de 2 horas");

                return "reserva/createResponse";
            }

            if (diffInHours < 3 && sala.get().getTipo() == TipoSala.SALA_ALTO_RISCO.toString()) {
                model.addAttribute(message, "Salas de alto risco tem um tempo minimo de reserva de 3 horas");

                return "reserva/createResponse";
            }

            if (inicio.getHourOfDay() < 6) {
                model.addAttribute(message, "Reservas nao podem iniciar antes das 06:00");

                return "reserva/createResponse";
            }

            if (fim.getHourOfDay() > 22 && fim.getMinuteOfHour() > 0) {
                model.addAttribute(message, "Reservas não podem terminar depois das 22:00");

                return "reserva/createResponse";
            }

            Optional<Reserva> salaJaOcupada = reservaRepository.checkSalaJaReservada(request.getIdSala(), inicio.toDate(), fim.toDate());

            if(salaJaOcupada.isPresent()){
                model.addAttribute(message, "Já existe uma reserva para a sala no horário informado");

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

            Reserva novaReserva = new Reserva(medico.get(), sala.get(), inicio, fim);

            reservaRepository.save(novaReserva);

            model.addAttribute(message, String.format("Reserva criada com sucesso! Medico %s na sala %s", medico.get().getNome(), sala.get().getNome()));

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

        //ver se datas tao null

        LocalDate inicio = dateFormat.parseLocalDate(dataInicio);

        LocalDate fim = dateFormat.parseLocalDate(dataFim);

        List<Reserva> reservas = reservaRepository.getReservasBetween(inicio.toDate(), fim.toDate());

        model.addAttribute("reservas", reservas);

        model.addAttribute("title", "Lista de reservas filtradas");

        return "reserva/list";
    }
}
