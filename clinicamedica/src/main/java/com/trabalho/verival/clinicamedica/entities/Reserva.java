package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;
import java.util.Date;
import com.trabalho.verival.clinicamedica.entities.Medico;
@Entity
@Table(name="RESERVA")
public class Reserva {
    public Reserva() {
    }

    public Reserva(Medico medico, Sala sala, Date dataInicio, Date dataFim) {
        this.medico = medico;
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESERVA_ID")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
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


    public Date getDataInicio() { return dataInicio; }

    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

    public Date getDataFim() { return dataFim; }

    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }

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
