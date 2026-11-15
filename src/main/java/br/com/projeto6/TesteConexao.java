package br.com.projeto6;

import br.com.projeto6.conexao.ConexaoBD;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {
        System.out.println("Testando conexao com o SQL Server...");

        try (Connection conexao = ConexaoBD.conectar()) {
            System.out.println("Conexao realizada com sucesso!");
            System.out.println("Banco conectado: " + conexao.getCatalog());

        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao banco de dados.");
            System.out.println("Detalhes do erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
