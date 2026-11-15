package br.com.projeto6;

import br.com.projeto6.dao.UsuarioDAO;
import br.com.projeto6.model.Usuario;

import java.time.LocalDate;

public class TesteUsuarioDAO {

    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();

        try {
            // 1. Inserir usuario de teste
            Usuario usuario = new Usuario();
            usuario.setCpf("98765432100");
            usuario.setNome("Usuario Teste DAO");
            usuario.setDataNascimento(LocalDate.of(2005, 8, 15));
            usuario.setCelular("19988887777");
            usuario.setEmail("teste.dao@email.com");

            int id = dao.inserir(usuario);
            System.out.println("Usuario inserido! ID: " + id);

            // 2. Listar usuarios
            System.out.println("\n--- USUARIOS CADASTRADOS ---");
            for (Usuario u : dao.listar()) {
                System.out.println(
                    u.getIdUsuario() + " | " +
                    u.getNome() + " | " +
                    u.getEmail()
                );
            }

            // 3. Atualizar usuario
            usuario.setIdUsuario(id);
            usuario.setNome("Usuario Teste Atualizado");

            boolean atualizado = dao.atualizar(usuario);
            System.out.println("\nAtualizado: " + atualizado);

            // 4. Excluir usuario de teste
            boolean excluido = dao.excluir(id);
            System.out.println("Excluido: " + excluido);

        } catch (Exception e) {
            System.out.println("Erro durante o teste:");
            e.printStackTrace();
        }
    }
}
