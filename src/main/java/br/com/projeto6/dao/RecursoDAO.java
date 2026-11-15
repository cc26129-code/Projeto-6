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

    public int inserir(Recurso recurso) throws SQLException {
        String sql = "{call reserva.sp_inserir_recurso(?, ?, ?, ?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setString(1, recurso.getCodigo());
            comando.setString(2, recurso.getNome());
            comando.setInt(3, recurso.getCapacidade());
            comando.setString(4, recurso.getLocalizacao());

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("id_recurso");
                }
            }
        }

        throw new SQLException("A procedure nao retornou o ID do recurso inserido.");
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

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() && resultado.getInt("linhas_afetadas") > 0;
            }
        }
    }

    public boolean excluir(int idRecurso) throws SQLException {
        String sql = "{call reserva.sp_excluir_recurso(?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setInt(1, idRecurso);

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() && resultado.getInt("linhas_afetadas") > 0;
            }
        }
    }
}
