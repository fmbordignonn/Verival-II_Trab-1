package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Entity
public class Reserva {
    public Reserva() {
    }

    public Reserva(Long id, Date dataInicio, Date dataFim, String nomeMedico) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.nomeMedico = nomeMedico;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    private Date dataFim;

    private String nomeMedico;


}
