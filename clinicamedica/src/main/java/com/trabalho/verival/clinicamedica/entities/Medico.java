package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;

@Entity
@Table(name = "MEDICO")
public class Medico {

    public Medico() {

    }

    public Medico(long id, String nome, String crm, Especialidade especialidade) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String nome;

    private String crm;

    private Especialidade especialidade;

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

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}