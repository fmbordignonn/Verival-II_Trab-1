package com.trabalho.verival.clinicamedica.repositories;

import com.trabalho.verival.clinicamedica.entities.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends CrudRepository<Reserva, Long> {
    @Query(value = "SELECT * FROM RESERVA R WHERE TRUNC(R.DATA_INICIO) BETWEEN :dataInicio AND :dataFim", nativeQuery = true)
    public List<Reserva> getReservasBetween(@Temporal(TemporalType.DATE) Date dataInicio, @Temporal(TemporalType.DATE) Date dataFim);

    @Query(value = "SELECT * FROM RESERVA R WHERE SALA_ID = :idSala AND ((TRUNC(R.DATA_INICIO) BETWEEN :dataInicio AND :dataFim) OR (TRUNC(R.DATA_FIM) BETWEEN :dataInicio AND :dataFim))",nativeQuery = true)
    public Optional<Reserva> checkSalaJaReservada(long idSala, @Temporal(TemporalType.DATE) Date dataInicio, @Temporal(TemporalType.DATE) Date dataFim);

    @Query(value="SELECT * FROM RESERVA R WHERE TRUNC(R.DATA_INICIO) >= TRUNC(SYSDATE)", nativeQuery = true)
    public List<Reserva> getReservasFuturas();

}
