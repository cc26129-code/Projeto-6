package br.com.projeto6.conexao;

import br.com.projeto6.config.ConfiguracaoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoBD {

    private ConexaoBD() {
    }

    public static Connection conectar() throws SQLException {
        try {
            return DriverManager.getConnection(
                    ConfiguracaoBD.getUrl(),
                    ConfiguracaoBD.getUsuario(),
                    ConfiguracaoBD.getSenha()
            );
        } catch (IllegalStateException e) {
            throw new SQLException(
                    "Configuracao do banco incompleta. Verifique DB_USER e DB_PASSWORD.",
                    e
            );
        }
    }
}
