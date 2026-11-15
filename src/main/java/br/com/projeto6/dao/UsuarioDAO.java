package br.com.projeto6.dao;

import br.com.projeto6.conexao.ConexaoBD;
import br.com.projeto6.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsavel pelo acesso aos dados da entidade Usuario.
 *
 * Todas as operacoes sao executadas por meio das Stored Procedures
 * do schema reserva no SQL Server.
 */
public class UsuarioDAO {

    /**
     * Cadastra um usuario e devolve o ID gerado pelo banco.
     *
     * @param usuario usuario que sera cadastrado
     * @return ID do usuario inserido
     * @throws SQLException caso ocorra um erro no banco de dados
     */
    public int inserir(Usuario usuario) throws SQLException {
        String sql = "{call reserva.sp_inserir_usuario(?, ?, ?, ?, ?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setString(1, usuario.getCpf());
            comando.setString(2, usuario.getNome());

            if (usuario.getDataNascimento() == null) {
                comando.setNull(3, Types.DATE);
            } else {
                comando.setDate(3, Date.valueOf(usuario.getDataNascimento()));
            }

            comando.setString(4, usuario.getCelular());
            comando.setString(5, usuario.getEmail());

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("id_usuario");
                }
            }
        }

        throw new SQLException("A procedure nao retornou o ID do usuario inserido.");
    }

    /**
     * Retorna todos os usuarios cadastrados.
     *
     * @return lista de usuarios
     * @throws SQLException caso ocorra um erro no banco de dados
     */
    public List<Usuario> listar() throws SQLException {
        String sql = "{call reserva.sp_listar_usuarios()}";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                Usuario usuario = new Usuario();

                usuario.setIdUsuario(resultado.getInt("id_usuario"));
                usuario.setCpf(resultado.getString("cpf"));
                usuario.setNome(resultado.getString("nome"));

                Date dataNascimento = resultado.getDate("data_nascimento");
                if (dataNascimento != null) {
                    usuario.setDataNascimento(dataNascimento.toLocalDate());
                }

                usuario.setCelular(resultado.getString("celular"));
                usuario.setEmail(resultado.getString("email"));

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    /**
     * Atualiza os dados de um usuario existente.
     *
     * @param usuario usuario com ID e novos dados
     * @return true se pelo menos uma linha foi alterada
     * @throws SQLException caso ocorra um erro no banco de dados
     */
    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "{call reserva.sp_atualizar_usuario(?, ?, ?, ?, ?, ?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setInt(1, usuario.getIdUsuario());
            comando.setString(2, usuario.getCpf());
            comando.setString(3, usuario.getNome());

            if (usuario.getDataNascimento() == null) {
                comando.setNull(4, Types.DATE);
            } else {
                comando.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            }

            comando.setString(5, usuario.getCelular());
            comando.setString(6, usuario.getEmail());

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() && resultado.getInt("linhas_afetadas") > 0;
            }
        }
    }

    /**
     * Exclui um usuario pelo ID.
     *
     * @param idUsuario identificador do usuario
     * @return true se pelo menos uma linha foi excluida
     * @throws SQLException caso ocorra um erro no banco de dados
     */
    public boolean excluir(int idUsuario) throws SQLException {
        String sql = "{call reserva.sp_excluir_usuario(?)}";

        try (Connection conexao = ConexaoBD.conectar();
             CallableStatement comando = conexao.prepareCall(sql)) {

            comando.setInt(1, idUsuario);

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() && resultado.getInt("linhas_afetadas") > 0;
            }
        }
    }
}
