package br.com.projeto6.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoBD {

    private ConexaoBD() {
    }

    public static Connection conectar() throws SQLException {
        String url = System.getenv("DB_URL");
        String usuario = System.getenv("DB_USER");
        String senha = System.getenv("DB_PASSWORD");

        if (url == null || usuario == null || senha == null) {
            throw new SQLException(
                    "Configure as variáveis de ambiente DB_URL, DB_USER e DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(url, usuario, senha);
    }
}
