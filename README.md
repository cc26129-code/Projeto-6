# Projeto 6 — Sistema de Reserva de Laboratórios e Salas

Projeto acadêmico desenvolvido por Eduardo Moura e Francisco Braz.

## Tecnologias
- Java 21
- Java Swing
- Maven
- JDBC
- Microsoft SQL Server

## Estrutura
- `conexao`: conexão com o banco de dados.
- `model`: classes de domínio.
- `dao`: acesso a dados e chamadas às procedures.
- `controller`: regras e coordenação da aplicação.
- `view`: interfaces gráficas Swing.

## Configuração do banco
A conexão será configurada por variáveis de ambiente, sem armazenar senhas no GitHub:

- `DB_URL`: URL JDBC completa do SQL Server.
- `DB_USER`: usuário do banco.
- `DB_PASSWORD`: senha do banco.

Não envie credenciais, senhas ou tokens para o repositório.

## Executar
Abra o projeto pelo arquivo `pom.xml` no IntelliJ IDEA, aguarde o Maven carregar as dependências e execute a classe `br.com.projeto6.Main`.
