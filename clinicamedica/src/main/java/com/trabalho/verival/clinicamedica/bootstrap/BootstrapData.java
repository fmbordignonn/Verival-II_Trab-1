package com.trabalho.verival.clinicamedica.bootstrap;

import com.trabalho.verival.clinicamedica.entities.*;
import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.joda.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BootstrapData implements CommandLineRunner {

    private final SalaRepository salaRepository;

    private final MedicoRepository medicoRepository;

    private final ReservaRepository reservaRepository;

    public BootstrapData(SalaRepository salaRepository, MedicoRepository medicoRepository, ReservaRepository reservaRepository) {
        this.salaRepository = salaRepository;
        this.medicoRepository = medicoRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Sala> salas = Stream.of(
                new Sala("Sala 1", TipoSala.SALA_PEQUENA),
                new Sala("Sala 2", TipoSala.SALA_PEQUENA),
                new Sala("Sala 3", TipoSala.SALA_PEQUENA),
                new Sala("Sala 4", TipoSala.SALA_GRANDE),
                new Sala("Sala 5", TipoSala.SALA_GRANDE),
                new Sala("Sala 6", TipoSala.SALA_GRANDE),
                new Sala("Sala 7", TipoSala.SALA_ALTO_RISCO),
                new Sala("Sala 8", TipoSala.SALA_ALTO_RISCO),
                new Sala("Sala 9", TipoSala.SALA_ALTO_RISCO),
                new Sala("Sala 10", TipoSala.SALA_ALTO_RISCO))
                .collect(Collectors.toList());

        salaRepository.saveAll(salas);

        List<Medico> medicos = Stream.of(
                new Medico("José", "12345", Especialidade.CIRURGIAO),
                new Medico("Carlos", "54321", Especialidade.DERMATOLOGISTA),
                new Medico("Pedro", "12341", Especialidade.NEUROLOGISTA),
                new Medico("Marcos", "65435", Especialidade.DERMATOLOGISTA),
                new Medico("Jesus", "54895", Especialidade.CIRURGIAO)
        ).collect(Collectors.toList());

        medicoRepository.saveAll(medicos);

        List<Reserva> reservas = Stream.of(
                new Reserva(medicos.stream().findFirst().get(),
                        salas.stream().findFirst().get(),
                        LocalDateTime.now(),
                        LocalDateTime.now())
        ).collect(Collectors.toList());

        reservaRepository.saveAll(reservas);

        for (Medico medico : medicoRepository.findAll()) {
            System.out.println(String.format("Medico %s com ID %d%n", medico.getNome(), medico.getId()));
        }

        System.out.println("---------------------------------------------------");

        for (Sala sala : salaRepository.findAll()) {
            System.out.println(String.format("Sala %s com ID %d%n", sala.getNome(), sala.getId()));
        }

//        System.out.println("Id de um medico: " + medicos.stream().findFirst().get().getId());
//        System.out.println("Id de uma sala: " + salas.stream().findFirst().get().getId());

    }
}
