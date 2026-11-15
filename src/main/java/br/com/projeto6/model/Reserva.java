package br.com.projeto6.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa uma reserva de laboratorio ou sala.
 */
public class Reserva {

    private Integer idReserva;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    private Integer idUsuario;
    private Integer idRecurso;
    private Integer idStatus;

    public Reserva() {
    }

    public Reserva(Integer idReserva, LocalDate dataInicial, LocalDate dataFinal,
                   LocalTime horaInicial, LocalTime horaFinal,
                   Integer idUsuario, Integer idRecurso, Integer idStatus) {
        this.idReserva = idReserva;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.idStatus = idStatus;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public LocalTime getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }
}
