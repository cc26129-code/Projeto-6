# Projeto 6 — Sistema de Reserva de Laboratórios e Salas

Projeto acadêmico desenvolvido por Eduardo Moura e Francisco Braz.

## Tecnologias
- Java 21
- Java Swing
- Maven
- JDBC
- Microsoft SQL Server

## Estrutura
- `config`: configuracoes da aplicacao e do banco de dados.
- `conexao`: abertura de conexoes JDBC.
- `model`: classes de dominio.
- `dao`: acesso a dados e chamadas às procedures.
- `controller`: regras e coordenacao da aplicacao.
- `view`: interfaces graficas Swing.

## Configuracao do banco

A classe `br.com.projeto6.config.ConfiguracaoBD` centraliza os dados de conexao. acho que foi

Por padrao, o projeto utiliza o servidor e banco de desenvolvimento:
`regulus.cotuca.unicamp.br:1433 / BD26129`.

Configure estas variaveis de ambiente na configuracao de execucao do IntelliJ:

- `DB_USER`: usuario do SQL Server.
- `DB_PASSWORD`: senha do SQL Server.
- `DB_URL`: URL JDBC completa (opcional; se omitida, sera usada a URL padrao acima).

Exemplo de URL:

```
jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databaseName=BD26129;encrypt=true;trustServerCertificate=true
```

Para trocar de servidor ou banco futuramente, basta alterar `DB_URL`. Para trocar o login, altere `DB_USER` e `DB_PASSWORD`.

Nunca coloque usuario, senha ou outras credenciais diretamente no codigo ou no GitHub.

## Executar
Abra o projeto pelo arquivo `pom.xml` no IntelliJ, aguarde o Maven carregar as dependencias e execute a classe `br.com.projeto6.Main`.
