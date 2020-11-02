package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MEDICO")
public class Medico {

    public Medico() {

    }

    public Medico(String nome, String crm, Especialidade especialidade) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade.toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEDICO_ID")
    public long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CRM")
    private String crm;

    @Column(name = "ESPECIALIDADE")
    private String especialidade;

    @OneToMany(mappedBy = "medico")
    private Set<Reserva> reservas;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Set<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    public String toString() {
        return String.format("%d%n - %s - %s", this.getId(), this.getNome(), this.getEspecialidade());
    }
}