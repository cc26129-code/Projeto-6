package br.com.projeto6.config;

/**
 * Centraliza as configuracoes de acesso ao SQL Server.
 *
 * O endereco padrao aponta para o banco usado no desenvolvimento.
 * Para trocar servidor ou banco, defina DB_URL nas variaveis de ambiente.
 * Nunca coloque usuario ou senha diretamente neste arquivo.
 */
public final class ConfiguracaoBD {

    private static final String URL_PADRAO =
            "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;"
                    + "databaseName=BD26129;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true";

    private ConfiguracaoBD() {
    }

    public static String getUrl() {
        return obterVariavel("DB_URL", URL_PADRAO);
    }

    public static String getUsuario() {
        return obterVariavelObrigatoria("DB_USER");
    }

    public static String getSenha() {
        return obterVariavelObrigatoria("DB_PASSWORD");
    }

    private static String obterVariavel(String nome, String valorPadrao) {
        String valor = System.getenv(nome);

        if (valor == null || valor.isBlank()) {
            return valorPadrao;
        }

        return valor.trim();
    }

    private static String obterVariavelObrigatoria(String nome) {
        String valor = System.getenv(nome);

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "A variavel de ambiente " + nome + " nao foi configurada."
            );
        }

        return valor;
    }
}
