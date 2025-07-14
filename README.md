# Identity Manager
![Status](https://img.shields.io/badge/status-active-success.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-green)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-yellow?logo=jsonwebtokens)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)](https://www.postgresql.org/)
![Build](https://img.shields.io/badge/Built%20with-Maven-red)
![License](https://img.shields.io/github/license/apolinario0x21/identity-manager)
[![Security](https://img.shields.io/badge/Security-Spring%20Security-brightgreen)](https://spring.io/projects/spring-security)

O projeto consiste em uma API RESTful que permite aos usuários se registrarem, fazerem login para obter um token de acesso e acessarem rotas protegidas. Ele também inclui funcionalidades como atualização de token (refresh token) e listagem de usuários com paginação.

## Funcionalidades
- Registro de Usuário: Endpoint para criar novos usuários.
- Autenticação de Usuário: Endpoint de login que retorna um JWT após a autenticação bem-sucedida.
- Autorização baseada em Token: Rotas protegidas que só podem ser acessadas com um JWT válido.
- Atualização de Token (Refresh Token): Endpoint para obter um novo token de acesso usando um refresh token.
- Segurança: Senhas são armazenadas de forma segura usando BCrypt.
- Paginação e Ordenação: Endpoint para listar usuários com suporte para paginação e ordenação.
- Tratamento de Exceções: Handler global para tratar erros de forma consistente.
- Testes Unitários: Testes para serviços e utilitários garantindo a qualidade do código.


## 🛠️ Tecnologias Utilizadas
- Java 17
- Spring Boot 3.4.4
- Spring Security: Para autenticação e controle de acesso.
- Spring Data JPA: Para persistência de dados.
- PostgreSQL: Banco de dados relacional.
- Maven: Gerenciador de dependências.
- JJWT (Java JWT): Para criação e validação dos tokens.
- JUnit 5 & Mockito: Para testes unitários.

## 🚀 Como Executar o Projeto
Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### Pré-requisitos
- Java Development Kit (JDK) 17 ou superior.
- Maven 3.8 ou superior.
- Uma instância do PostgreSQL em execução.

#### 1. Clonar o Repositório
  
  ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd jwt-auth
  ```

#### 2. Configurar o Banco de Dados
  - Acesse seu servidor PostgreSQL e crie um novo banco de dados. Por exemplo: identity_manager_db.
  - Abra o arquivo src/main/resources/application.properties.
  - Atualize as seguintes propriedades com as suas credenciais do PostgreSQL:

```Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/identity_manager_db
spring.datasource.username=seu_usuario_postgres
spring.datasource.password=sua_senha_postgres
```

#### 3. Configurar Variáveis de Ambiente
É uma boa prática não commitar segredos. No arquivo application.properties, altere o segredo do JWT para um valor único e seguro.

```Properties
# Gere uma chave secreta forte para produção
jwt.secret=sua_chave_secreta_super_segura_aqui
jwt.expiration=900000 # 15 minutos
```

#### 4. Instalar Dependências e Executar
O Maven cuidará do download de todas as dependências necessárias.

```Bash

# Para instalar as dependências e executar o projeto
mvn spring-boot:run
```

A aplicação estará disponível em http://localhost:8080.


## 🧪 Executando os Testes
Para rodar a suíte de testes unitários e garantir que tudo está funcionando como esperado, execute o comando:

```Bash
mvn test
```

## 📡 Endpoints da API
A coleção de endpoints da API está disponível no prefixo /.

| Método   | Endpoint       | Protegido  | asd |
| :- | :- | :- | :-
| `POST` | `/auth/register` | Não | Registra um novo usuário. |
| `POST` | `/auth/login` | Não | Autentica um usuário e retorna um JWT. |
| `POST` | `/auth/refresh-token` | Não | Gera um novo token de acesso a partir de um refresh token. |
| `GET` | `/api/protected` | Sim | Rota de exemplo que requer autenticação. |
| `GET` | `/users` | Sim | Lista todos os usuários com paginação e ordenação. |

## Exemplo de Requisições
### Registrar Usuário - `POST /auth/register`
```json
{
    "username": "novo_usuario",
    "password": "senha_forte_123",
    "email": "usuario@exemplo.com"
}
```

### Fazer Login - `POST /auth/login`
```json
{
    "username": "novo_usuario",
    "password": "senha_forte_123"
}
```

### Acessar Rota Protegida - `GET /api/protected`
Header:
`Authorization: Bearer <seu_token_jwt>`

### Listar Usuários com Paginação - `GET /users?page=0&size=5&sortBy=username&sortDirection=asc`
Header:
`Authorization: Bearer <seu_token_jwt>`


## 📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](https://github.com/apolinario0x21/identity-manager/blob/main/LICENSE) para mais detalhes.
