package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;
import org.joda.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="RESERVA")
public class Reserva {
    public Reserva() {
    }

    public Reserva(Medico medico, Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.medico = medico;
        this.sala = sala;
        this.dataInicio = dataInicio.toDate();
        this.dataFim = dataFim.toDate();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVA_ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_FIM")
    private Date dataFim;

    @ManyToOne
    @JoinColumn(name="MEDICO_ID")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name="SALA_ID")
    private Sala sala;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }


    public LocalDateTime getDataInicio() { return new LocalDateTime(this.dataInicio); }

    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio.toDate(); }

    public LocalDateTime getDataFim() { return new LocalDateTime(this.dataFim); }

    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim.toDate(); }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}
