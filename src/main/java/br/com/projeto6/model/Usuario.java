package br.com.projeto6.model;

import java.time.LocalDate;

/**
 * Representa um usuario cadastrado no sistema.
 */
public class Usuario {

    private Integer idUsuario;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String celular;
    private String email;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String cpf, String nome,
                   LocalDate dataNascimento, String celular, String email) {
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.celular = celular;
        this.email = email;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
