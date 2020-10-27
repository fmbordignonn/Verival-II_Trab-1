package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;

@Entity
@Table(name = "SALA")
public class Sala {
    public Sala() {

    }

    public Sala(String nome, TipoSala tipo, double custo) {
        this.nome = nome;
        this.tipo = tipo;
        this.custo = custo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nome;

    private TipoSala tipo;

    private double custo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }
}