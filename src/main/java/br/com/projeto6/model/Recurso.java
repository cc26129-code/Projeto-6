package br.com.projeto6.model;

/**
 * Representa um laboratorio ou sala que pode ser reservado.
 */
public class Recurso {

    private Integer idRecurso;
    private String codigo;
    private String nome;
    private Integer capacidade;
    private String localizacao;

    public Recurso() {
    }

    public Recurso(Integer idRecurso, String codigo, String nome,
                   Integer capacidade, String localizacao) {
        this.idRecurso = idRecurso;
        this.codigo = codigo;
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
