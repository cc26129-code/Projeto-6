package br.com.projeto6;

import br.com.projeto6.dao.RecursoDAO;
import br.com.projeto6.model.Recurso;

public class TesteRecursoDAO {

    public static void main(String[] args) {
        RecursoDAO dao = new RecursoDAO();

        try {
            // 1. Inserir recurso de teste
            Recurso recurso = new Recurso();
            recurso.setCodigo("TESTE-DAO-001");
            recurso.setNome("Laboratorio Teste DAO");
            recurso.setCapacidade(20);
            recurso.setLocalizacao("Bloco de Testes");

            int id = dao.inserir(recurso);
            System.out.println("Recurso inserido! ID: " + id);

            // 2. Listar recursos
            System.out.println("\n--- RECURSOS CADASTRADOS ---");
            for (Recurso r : dao.listar()) {
                System.out.println(
                    r.getIdRecurso() + " | " +
                    r.getCodigo() + " | " +
                    r.getNome() + " | " +
                    r.getCapacidade() + " | " +
                    r.getLocalizacao()
                );
            }

            // 3. Atualizar recurso
            recurso.setIdRecurso(id);
            recurso.setNome("Laboratorio Teste DAO Atualizado");

            boolean atualizado = dao.atualizar(recurso);
            System.out.println("\nAtualizado: " + atualizado);

            // 4. Excluir recurso de teste
            boolean excluido = dao.excluir(id);
            System.out.println("Excluido: " + excluido);

        } catch (Exception e) {
            System.out.println("Erro durante o teste:");
            e.printStackTrace();
        }
    }
}
