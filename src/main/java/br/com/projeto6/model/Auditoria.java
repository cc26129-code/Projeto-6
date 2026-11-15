package br.com.projeto6.model;

import java.time.LocalDateTime;

/**
 * Representa um registro de acesso ao sistema.
 */
public class Auditoria {

    private Integer idAuditoria;
    private Integer idUsuario;
    private LocalDateTime dataHora;

    public Auditoria() {
    }

    public Auditoria(Integer idAuditoria, Integer idUsuario, LocalDateTime dataHora) {
        this.idAuditoria = idAuditoria;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
