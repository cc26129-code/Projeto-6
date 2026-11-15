package br.com.projeto6.model;

import java.time.LocalDateTime;

/**
 * Representa as credenciais de acesso de um usuario.
 */
public class Login {

    private Integer idLogin;
    private Integer idUsuario;
    private String login;
    private String senha;
    private LocalDateTime dataCadastro;

    public Login() {
    }

    public Login(Integer idLogin, Integer idUsuario, String login,
                 String senha, LocalDateTime dataCadastro) {
        this.idLogin = idLogin;
        this.idUsuario = idUsuario;
        this.login = login;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
    }

    public Integer getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(Integer idLogin) {
        this.idLogin = idLogin;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
