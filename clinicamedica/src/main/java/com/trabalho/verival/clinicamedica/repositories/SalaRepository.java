package com.trabalho.verival.clinicamedica.repositories;

import com.trabalho.verival.clinicamedica.entities.Sala;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SalaRepository extends CrudRepository<Sala, Long> {
    public List<Sala> findAllByOrderByTipoDesc();
}
