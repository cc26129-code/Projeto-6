package br.com.projeto6.model;

/**
 * Representa um status possivel para uma reserva.
 */
public class StatusReserva {

    private Integer idStatus;
    private String nome;
    private String descricao;

    public StatusReserva() {
    }

    public StatusReserva(Integer idStatus, String nome, String descricao) {
        this.idStatus = idStatus;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
