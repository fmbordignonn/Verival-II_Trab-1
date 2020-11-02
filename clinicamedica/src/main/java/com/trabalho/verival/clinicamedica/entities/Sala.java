package com.trabalho.verival.clinicamedica.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SALA")
public class Sala {
    public Sala() {

    }

    public Sala(String nome, TipoSala tipo) {
        this.nome = nome;
        this.tipo = tipo.toString();
        switch (tipo) {
            case SALA_PEQUENA:
                this.custo = 400;
                break;

            case SALA_GRANDE:
                this.custo = 650;
                break;

            case SALA_ALTO_RISCO:
                this.custo = 1200;
                break;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SALA_ID")
    private long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TIPO_SALA")
    private String tipo;

    @Column(name = "CUSTO")
    private double custo;

    @OneToMany(mappedBy = "sala")
    private Set<Reserva> reservas;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public Set<Reserva> getReservas() { return reservas; }

    public void setReservas(Set<Reserva> reservas) { this.reservas = reservas; }

    public double getCustoTotalGerado(){
        double custo = this.getReservas().stream().mapToDouble(reserva -> reserva.getCustoTotal()).sum();

        return custo;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.nome, this.tipo);
    }
}