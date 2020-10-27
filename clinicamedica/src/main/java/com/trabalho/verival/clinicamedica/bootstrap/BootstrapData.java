package com.trabalho.verival.clinicamedica.bootstrap;

import com.trabalho.verival.clinicamedica.entities.Sala;
import com.trabalho.verival.clinicamedica.entities.TipoSala;
import com.trabalho.verival.clinicamedica.repositories.SalaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final SalaRepository salaRepository;

    public BootstrapData(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Sala sala = new Sala("Salinha", TipoSala.SALA_ALTO_RISCO, 100.21);

        salaRepository.save(sala);
    }
}
