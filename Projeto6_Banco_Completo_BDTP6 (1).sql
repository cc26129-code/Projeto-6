-- PROJETO 6 - SCRIPT COMPLETO DO BANCO DE DADOS
-- Reúne schema, tabelas, dados iniciais de status, views, trigger e 24 procedures.
-- Execute em um banco vazio. Se as tabelas/objetos já existirem, NÃO execute este script inteiro novamente.

USE BDTP6;
GO

CREATE SCHEMA reserva;
GO

CREATE TABLE reserva.USUARIO (
    id_usuario INT IDENTITY(1,1) NOT NULL,
    cpf CHAR(11) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    celular VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT PK_USUARIO PRIMARY KEY (id_usuario),
    CONSTRAINT UQ_USUARIO_CPF UNIQUE (cpf),
    CONSTRAINT UQ_USUARIO_EMAIL UNIQUE (email)
);
GO

CREATE TABLE reserva.RECURSO (
    id_recurso INT IDENTITY(1,1) NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    localizacao VARCHAR(100) NOT NULL,
    CONSTRAINT PK_RECURSO PRIMARY KEY (id_recurso),
    CONSTRAINT UQ_RECURSO_CODIGO UNIQUE (codigo),
    CONSTRAINT CK_RECURSO_CAPACIDADE CHECK (capacidade > 0)
);
GO

CREATE TABLE reserva.STATUS_RESERVA (
    id_status INT IDENTITY(1,1) NOT NULL,
    nome VARCHAR(30) NOT NULL,
    descricao VARCHAR(200),
    CONSTRAINT PK_STATUS_RESERVA PRIMARY KEY (id_status),
    CONSTRAINT UQ_STATUS_RESERVA_NOME UNIQUE (nome)
);
GO

CREATE TABLE reserva.LOGIN (
    id_login INT IDENTITY(1,1) NOT NULL,
    id_usuario INT NOT NULL,
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT PK_LOGIN PRIMARY KEY (id_login),
    CONSTRAINT FK_LOGIN_USUARIO FOREIGN KEY (id_usuario) REFERENCES reserva.USUARIO(id_usuario),
    CONSTRAINT UQ_LOGIN_USUARIO UNIQUE (id_usuario),
    CONSTRAINT UQ_LOGIN_LOGIN UNIQUE (login)
);
GO

CREATE TABLE reserva.AUDITORIA (
    id_auditoria INT IDENTITY(1,1) NOT NULL,
    id_usuario INT NOT NULL,
    data_hora DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT PK_AUDITORIA PRIMARY KEY (id_auditoria),
    CONSTRAINT FK_AUDITORIA_USUARIO FOREIGN KEY (id_usuario) REFERENCES reserva.USUARIO(id_usuario)
);
GO

CREATE TABLE reserva.RESERVA (
    id_reserva INT IDENTITY(1,1) NOT NULL,
    data_inicial DATE NOT NULL,
    data_final DATE NOT NULL,
    hora_inicial TIME NOT NULL,
    hora_final TIME NOT NULL,
    id_usuario INT NOT NULL,
    id_recurso INT NOT NULL,
    id_status INT NOT NULL,
    CONSTRAINT PK_RESERVA PRIMARY KEY (id_reserva),
    CONSTRAINT FK_RESERVA_USUARIO FOREIGN KEY (id_usuario) REFERENCES reserva.USUARIO(id_usuario),
    CONSTRAINT FK_RESERVA_RECURSO FOREIGN KEY (id_recurso) REFERENCES reserva.RECURSO(id_recurso),
    CONSTRAINT FK_RESERVA_STATUS FOREIGN KEY (id_status) REFERENCES reserva.STATUS_RESERVA(id_status),
    CONSTRAINT CK_RESERVA_DATA CHECK (data_final >= data_inicial),
    CONSTRAINT CK_RESERVA_HORA CHECK (hora_final > hora_inicial)
);
GO

INSERT INTO reserva.STATUS_RESERVA (nome, descricao) VALUES
('Livre', 'Recurso disponível para reserva'),
('Ocupado', 'Recurso em utilização'),
('Bloqueado', 'Recurso indisponível para reserva'),
('Reservado', 'Recurso reservado por um usuário');
GO

-- VIEW: informações completas das reservas
CREATE OR ALTER VIEW reserva.vw_reservas
AS
SELECT
    r.id_reserva,
    rec.nome AS nome_recurso,
    rec.capacidade,
    rec.localizacao,
    r.data_inicial,
    r.data_final,
    r.hora_inicial,
    r.hora_final,
    st.nome AS nome_status,
    u.nome AS nome_usuario,
    u.email AS email_usuario
FROM reserva.RESERVA r
INNER JOIN reserva.RECURSO rec ON r.id_recurso = rec.id_recurso
INNER JOIN reserva.STATUS_RESERVA st ON r.id_status = st.id_status
INNER JOIN reserva.USUARIO u ON r.id_usuario = u.id_usuario;
GO

-- VIEW: histórico de acessos dos usuários
CREATE OR ALTER VIEW reserva.vw_auditoria_acessos
AS
SELECT
    a.id_auditoria,
    a.data_hora,
    u.id_usuario,
    u.nome AS nome_usuario,
    u.email AS email_usuario
FROM reserva.AUDITORIA a
INNER JOIN reserva.USUARIO u ON a.id_usuario = u.id_usuario;
GO

-- TRIGGER: impede sobreposição de reservas do mesmo recurso.
-- Reservas com status Reservado ou Ocupado são consideradas para conflito.
-- Intervalos consecutivos são permitidos: início < fim_existente e fim > início_existente.
CREATE OR ALTER TRIGGER reserva.tr_verificar_conflito_reserva
ON reserva.RESERVA
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM inserted i
        INNER JOIN reserva.RESERVA r
            ON r.id_recurso = i.id_recurso
           AND r.id_reserva <> i.id_reserva
        INNER JOIN reserva.STATUS_RESERVA si
            ON si.id_status = i.id_status
        INNER JOIN reserva.STATUS_RESERVA sr
            ON sr.id_status = r.id_status
        CROSS APPLY (SELECT
            CAST(CONCAT(CONVERT(VARCHAR(10), i.data_inicial, 23), ' ', CONVERT(VARCHAR(16), i.hora_inicial, 114)) AS DATETIME2) AS inicio_i,
            CAST(CONCAT(CONVERT(VARCHAR(10), i.data_final, 23), ' ', CONVERT(VARCHAR(16), i.hora_final, 114)) AS DATETIME2) AS fim_i,
            CAST(CONCAT(CONVERT(VARCHAR(10), r.data_inicial, 23), ' ', CONVERT(VARCHAR(16), r.hora_inicial, 114)) AS DATETIME2) AS inicio_r,
            CAST(CONCAT(CONVERT(VARCHAR(10), r.data_final, 23), ' ', CONVERT(VARCHAR(16), r.hora_final, 114)) AS DATETIME2) AS fim_r
        ) d
        WHERE si.nome IN ('Reservado', 'Ocupado')
          AND sr.nome IN ('Reservado', 'Ocupado')
          AND d.inicio_i < d.fim_r
          AND d.fim_i > d.inicio_r
    )
    BEGIN
        THROW 50001, 'Conflito: o recurso ja possui uma reserva nesse intervalo.', 1;
    END;
END;
GO

-- sp_atualizar_auditoria
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_auditoria
    @id_auditoria INT,
    @id_usuario INT,
    @data_hora DATETIME2
AS
BEGIN
    UPDATE reserva.AUDITORIA
    SET
        id_usuario = @id_usuario,
        data_hora = @data_hora
    WHERE id_auditoria = @id_auditoria;
END
GO
-- sp_atualizar_login
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_login
    @id_login INT,
    @login VARCHAR(50),
    @senha VARCHAR(255)
AS
BEGIN
    UPDATE reserva.LOGIN
    SET
        login = @login,
        senha = @senha
    WHERE id_login = @id_login;
END
GO
-- sp_atualizar_recurso
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_recurso
    @id_recurso INT,
    @codigo VARCHAR(20),
    @nome VARCHAR(100),
    @capacidade INT,
    @localizacao VARCHAR(100)
AS
BEGIN
    UPDATE reserva.RECURSO
    SET
        codigo = @codigo,
        nome = @nome,
        capacidade = @capacidade,
        localizacao = @localizacao
    WHERE id_recurso = @id_recurso;
END
GO
-- sp_atualizar_reserva
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_reserva
    @id_reserva INT,
    @data_inicial DATE,
    @data_final DATE,
    @hora_inicial TIME,
    @hora_final TIME,
    @id_usuario INT,
    @id_recurso INT,
    @id_status INT
AS
BEGIN
    UPDATE reserva.RESERVA
    SET
        data_inicial = @data_inicial,
        data_final = @data_final,
        hora_inicial = @hora_inicial,
        hora_final = @hora_final,
        id_usuario = @id_usuario,
        id_recurso = @id_recurso,
        id_status = @id_status
    WHERE id_reserva = @id_reserva;
END
GO
-- sp_atualizar_status
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_status
    @id_status INT,
    @nome VARCHAR(30),
    @descricao VARCHAR(200)
AS
BEGIN
    UPDATE reserva.STATUS_RESERVA
    SET nome = @nome,
        descricao = @descricao
    WHERE id_status = @id_status;
END
GO
-- sp_atualizar_usuario
CREATE OR ALTER PROCEDURE reserva.sp_atualizar_usuario
    @id_usuario INT,
    @cpf CHAR(11),
    @nome VARCHAR(100),
    @data_nascimento DATE,
    @celular VARCHAR(20),
    @email VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE reserva.USUARIO
    SET
        cpf = @cpf,
        nome = @nome,
        data_nascimento = @data_nascimento,
        celular = @celular,
        email = @email
    WHERE id_usuario = @id_usuario;

    SELECT @@ROWCOUNT AS linhas_afetadas;
END
GO
-- sp_excluir_auditoria
CREATE OR ALTER PROCEDURE reserva.sp_excluir_auditoria
    @id_auditoria INT
AS
BEGIN
    DELETE FROM reserva.AUDITORIA
    WHERE id_auditoria = @id_auditoria;
END
GO
-- sp_excluir_login
CREATE OR ALTER PROCEDURE reserva.sp_excluir_login
    @id_login INT
AS
BEGIN
    DELETE FROM reserva.LOGIN
    WHERE id_login = @id_login;
END
GO
-- sp_excluir_recurso
CREATE OR ALTER PROCEDURE reserva.sp_excluir_recurso
    @id_recurso INT
AS
BEGIN
    DELETE FROM reserva.RECURSO
    WHERE id_recurso = @id_recurso;
END
GO
-- sp_excluir_reserva
CREATE OR ALTER PROCEDURE reserva.sp_excluir_reserva
    @id_reserva INT
AS
BEGIN
    DELETE FROM reserva.RESERVA
    WHERE id_reserva = @id_reserva;
END
GO
-- sp_excluir_status
CREATE OR ALTER PROCEDURE reserva.sp_excluir_status
    @id_status INT
AS
BEGIN
    DELETE FROM reserva.STATUS_RESERVA
    WHERE id_status = @id_status;
END
GO
-- sp_excluir_usuario
CREATE OR ALTER PROCEDURE reserva.sp_excluir_usuario
    @id_usuario INT
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM reserva.USUARIO
    WHERE id_usuario = @id_usuario;

    SELECT @@ROWCOUNT AS linhas_afetadas;
END
GO
-- sp_inserir_auditoria
CREATE OR ALTER PROCEDURE reserva.sp_inserir_auditoria
    @id_usuario INT
AS
BEGIN
    INSERT INTO reserva.AUDITORIA (id_usuario)
    VALUES (@id_usuario);
END
GO
-- sp_inserir_login
CREATE OR ALTER PROCEDURE reserva.sp_inserir_login
    @id_usuario INT,
    @login VARCHAR(50),
    @senha VARCHAR(255)
AS
BEGIN
    INSERT INTO reserva.LOGIN
        (id_usuario, login, senha)
    VALUES
        (@id_usuario, @login, @senha);
END
GO
-- sp_inserir_recurso
CREATE OR ALTER PROCEDURE reserva.sp_inserir_recurso
    @codigo VARCHAR(20),
    @nome VARCHAR(100),
    @capacidade INT,
    @localizacao VARCHAR(100)
AS
BEGIN
    INSERT INTO reserva.RECURSO
        (codigo, nome, capacidade, localizacao)
    VALUES
        (@codigo, @nome, @capacidade, @localizacao);
END
GO
-- sp_inserir_reserva
CREATE OR ALTER PROCEDURE reserva.sp_inserir_reserva
    @data_inicial DATE,
    @data_final DATE,
    @hora_inicial TIME,
    @hora_final TIME,
    @id_usuario INT,
    @id_recurso INT,
    @id_status INT
AS
BEGIN
    INSERT INTO reserva.RESERVA
        (data_inicial, data_final, hora_inicial, hora_final,
         id_usuario, id_recurso, id_status)
    VALUES
        (@data_inicial, @data_final, @hora_inicial, @hora_final,
         @id_usuario, @id_recurso, @id_status);
END
GO
-- sp_inserir_status
CREATE OR ALTER PROCEDURE reserva.sp_inserir_status
    @nome VARCHAR(30),
    @descricao VARCHAR(200)
AS
BEGIN
    INSERT INTO reserva.STATUS_RESERVA (nome, descricao)
    VALUES (@nome, @descricao);
END
GO
-- sp_inserir_usuario
CREATE OR ALTER PROCEDURE reserva.sp_inserir_usuario
    @cpf CHAR(11),
    @nome VARCHAR(100),
    @data_nascimento DATE,
    @celular VARCHAR(20),
    @email VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO reserva.USUARIO
    (cpf, nome, data_nascimento, celular, email)
    VALUES
    (@cpf, @nome, @data_nascimento, @celular, @email);

    SELECT CAST(SCOPE_IDENTITY() AS INT) AS id_usuario;
END
GO
-- sp_listar_auditoria
CREATE OR ALTER PROCEDURE reserva.sp_listar_auditoria
AS
BEGIN
    SELECT *
    FROM reserva.vw_auditoria_acessos
    ORDER BY data_hora DESC;
END
GO
-- sp_listar_logins
CREATE OR ALTER PROCEDURE reserva.sp_listar_logins
AS
BEGIN
    SELECT
        l.id_login,
        l.id_usuario,
        u.nome AS nome_usuario,
        l.login,
        l.data_cadastro
    FROM reserva.LOGIN l
    INNER JOIN reserva.USUARIO u
        ON l.id_usuario = u.id_usuario
    ORDER BY l.id_login;
END
GO
-- sp_listar_recursos
CREATE OR ALTER PROCEDURE reserva.sp_listar_recursos
AS
BEGIN
    SELECT
        id_recurso,
        codigo,
        nome,
        capacidade,
        localizacao
    FROM reserva.RECURSO
    ORDER BY id_recurso;
END
GO
-- sp_listar_reservas
CREATE OR ALTER PROCEDURE reserva.sp_listar_reservas
AS
BEGIN
    SELECT *
    FROM reserva.vw_reservas
    ORDER BY data_inicial, hora_inicial;
END
GO
-- sp_listar_status
CREATE OR ALTER PROCEDURE reserva.sp_listar_status
AS
BEGIN
    SELECT *
    FROM reserva.STATUS_RESERVA
    ORDER BY id_status;
END
GO
-- sp_listar_usuarios
CREATE OR ALTER PROCEDURE reserva.sp_listar_usuarios
AS
BEGIN
    SET NOCOUNT ON;

    SELECT *
    FROM reserva.USUARIO;
END
GO
