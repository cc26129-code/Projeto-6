package br.com.projeto6.dao;

import br.com.projeto6.conexao.ConexaoBD;
import br.com.projeto6.model.Recurso;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {

    public boolean inserir(Recurso recurso) throws SQLException {
        String sql = "{call reserva.sp_inserir_recurso(?, ?, ?, ?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setString(1, recurso.getCodigo());
            comando.setString(2, recurso.getNome());
            comando.setInt(3, recurso.getCapacidade());
            comando.setString(4, recurso.getLocalizacao());

            return executarDml(comando) > 0;
        }
    }

    public List<Recurso> listar() throws SQLException {
        String sql = "{call reserva.sp_listar_recursos()}";
        List<Recurso> recursos = new ArrayList<>();

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                Recurso recurso = new Recurso();
                recurso.setIdRecurso(resultado.getInt("id_recurso"));
                recurso.setCodigo(resultado.getString("codigo"));
                recurso.setNome(resultado.getString("nome"));
                recurso.setCapacidade(resultado.getInt("capacidade"));
                recurso.setLocalizacao(resultado.getString("localizacao"));
                recursos.add(recurso);
            }
        }

        return recursos;
    }

    public boolean atualizar(Recurso recurso) throws SQLException {
        String sql = "{call reserva.sp_atualizar_recurso(?, ?, ?, ?, ?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setInt(1, recurso.getIdRecurso());
            comando.setString(2, recurso.getCodigo());
            comando.setString(3, recurso.getNome());
            comando.setInt(4, recurso.getCapacidade());
            comando.setString(5, recurso.getLocalizacao());

            return executarDml(comando) > 0;
        }
    }

    public boolean excluir(int idRecurso) throws SQLException {
        String sql = "{call reserva.sp_excluir_recurso(?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setInt(1, idRecurso);

            return executarDml(comando) > 0;
        }
    }

    private int executarDml(CallableStatement comando) throws SQLException {
        int linhasAfetadas = 0;
        boolean temResultado = comando.execute();

        while (true) {
            if (temResultado) {
                try (ResultSet resultado = comando.getResultSet()) {
                    // As procedures de insercao, atualizacao e exclusao
                    // nao retornam conjuntos de resultados.
                }
            } else {
                int quantidade = comando.getUpdateCount();
                if (quantidade == -1) {
                    break;
                }
                linhasAfetadas += quantidade;
            }

            temResultado = comando.getMoreResults();
        }

        return linhasAfetadas;
    }
}
