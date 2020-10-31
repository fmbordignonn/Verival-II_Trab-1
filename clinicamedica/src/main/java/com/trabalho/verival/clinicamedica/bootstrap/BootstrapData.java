package com.trabalho.verival.clinicamedica.bootstrap;

import com.trabalho.verival.clinicamedica.entities.*;
import com.trabalho.verival.clinicamedica.repositories.MedicoRepository;
import com.trabalho.verival.clinicamedica.repositories.ReservaRepository;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
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
                new Sala("Alfa", TipoSala.SALA_PEQUENA),
                new Sala("Beta", TipoSala.SALA_PEQUENA),
                new Sala("Gama", TipoSala.SALA_PEQUENA),
                new Sala("Delta", TipoSala.SALA_GRANDE),
                new Sala("Epsilon", TipoSala.SALA_GRANDE),
                new Sala("Zeta", TipoSala.SALA_GRANDE),
                new Sala("Eta", TipoSala.SALA_ALTO_RISCO),
                new Sala("Teta", TipoSala.SALA_ALTO_RISCO),
                new Sala("Iota", TipoSala.SALA_ALTO_RISCO),
                new Sala("Kappa", TipoSala.SALA_ALTO_RISCO))
                .collect(Collectors.toList());

        salaRepository.saveAll(salas);

        List<Medico> medicos = Stream.of(
                new Medico("José","12345", Especialidade.CIRURGIAO),
                new Medico("José","12345", Especialidade.DERMATOLOGISTA),
                new Medico("José","12345", Especialidade.NEUROLOGISTA),
                new Medico("José","12345", Especialidade.DERMATOLOGISTA),
                new Medico("José","12345", Especialidade.CIRURGIAO)
        ).collect(Collectors.toList());

        medicoRepository.saveAll(medicos);

        List<Reserva> reservas = Stream.of(
                new Reserva(medicos.stream().findFirst().get(), salas.stream().findFirst().get(), new Date(), new Date())
        ).collect(Collectors.toList());

        reservaRepository.saveAll(reservas);

    }
}
