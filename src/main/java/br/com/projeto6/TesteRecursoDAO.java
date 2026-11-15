package br.com.projeto6;

import br.com.projeto6.dao.RecursoDAO;
import br.com.projeto6.model.Recurso;

public class TesteRecursoDAO {

    public static void main(String[] args) {
        RecursoDAO dao = new RecursoDAO();
        String codigoTeste = "TESTE-DAO-001";

        try {
            // 1. Verificar se o registro de teste ja existe.
            // Isso evita erro de codigo duplicado se uma execucao anterior
            // tiver inserido o recurso antes de apresentar uma falha.
            Recurso recurso = null;

            for (Recurso r : dao.listar()) {
                if (codigoTeste.equals(r.getCodigo())) {
                    recurso = r;
                    break;
                }
            }

            // 2. Inserir caso ainda nao exista.
            if (recurso == null) {
                recurso = new Recurso();
                recurso.setCodigo(codigoTeste);
                recurso.setNome("Laboratorio Teste DAO");
                recurso.setCapacidade(20);
                recurso.setLocalizacao("Bloco de Testes");

                boolean inserido = dao.inserir(recurso);
                System.out.println("Inserido: " + inserido);

                if (!inserido) {
                    throw new IllegalStateException(
                        "A procedure nao confirmou a insercao do recurso."
                    );
                }

                // A procedure atual nao retorna o ID; buscamos o registro
                // pela coluna codigo, que e unica na tabela.
                for (Recurso r : dao.listar()) {
                    if (codigoTeste.equals(r.getCodigo())) {
                        recurso = r;
                        break;
                    }
                }
            } else {
                System.out.println(
                    "Recurso de teste ja existia. ID: " + recurso.getIdRecurso()
                );
            }

            if (recurso.getIdRecurso() == null) {
                throw new IllegalStateException(
                    "Nao foi possivel localizar o ID do recurso de teste."
                );
            }

            int id = recurso.getIdRecurso();

            // 3. Listar recursos.
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

            // 4. Atualizar recurso.
            recurso.setNome("Laboratorio Teste DAO Atualizado");
            boolean atualizado = dao.atualizar(recurso);
            System.out.println("\nAtualizado: " + atualizado);

            // 5. Excluir recurso de teste.
            boolean excluido = dao.excluir(id);
            System.out.println("Excluido: " + excluido);

        } catch (Exception e) {
            System.out.println("Erro durante o teste:");
            e.printStackTrace();
        }
    }
}
