# Project Health Check API

API em **Spring Boot** usando **JPA**, **PostgreSQL** e **Flyway** para
gerenciar projetos, health checks e resultados de verificação.

## Tecnologias

-   Java 17+
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   PostgreSQL
-   Flyway
-   Maven

## Como rodar

### 1. Criar banco

``` sql
CREATE DATABASE healthcheck;
```

### 2. Configurar `application.properties`

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/healthcheck
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### 3. Rodar o projeto

``` bash
mvn spring-boot:run
```

## Migrações Flyway

Arquivos ficam em:

    src/main/resources/db/migration

Nunca editar migrações antigas. Criar novas versões sempre.

## Endpoints

### Projects

  | Método | Rota                    | Descrição  |
  |:------:|:------------------------|:----------:|
  |  POST  | `/api/v1/projects`      |   Criar    |
  |  GET   | `/api/v1/projects`      |   Listar   |
  |  GET   | `/api/v1/projects/{id}` |   Buscar   |
  |  PUT   | `/api/v1/projects/{id}` | Atualizar  |
  | DELETE | `/api/v1/projects/{id}` |  Deletar   |

### Health Checks

  | Método  | Rota                                              | Descrição |
  |:-------:|:--------------------------------------------------|:---------:|
  |   GET   | `/api/v1/health-checks/{projectId}/health-checks` |  Listar   |
  |   GET   | `/api/v1/health-checks/{healthCheckId}`           |  Buscar   |

### Log Entry

| Método  | Rota                            | Descrição |
|:-------:|:--------------------------------|:---------:|
|   GET   | `/api/v1/logs/{projectId}/logs` |  Listar   |
|   GET   | `/api/v1/logs/{id}`             |  Buscar   |

### Incidents

| Método  | Rota                                      | Descrição |
|:-------:|:------------------------------------------|:---------:|
|   GET   | `/api/v1/incidents/{projectId}/incidents` |  Listar   |
|   GET   | `/api/v1/incidents/{id}`                  |  Buscar   |

### Log Exceptions

| Método  | Rota                                  | Descrição |
|:-------:|:--------------------------------------|:---------:|
|   GET   | `/api/v1/logs/{projectId}/exceptions` |  Listar   |
|   GET   | `/api/v1/logs/exception/{id}`         |  Buscar   |



## Docker

### Banco

``` yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: healthcheck
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: senha
    ports:
      - "5432:5432"
```

### App

``` bash
mvn clean package
docker build -t healthcheck-api .
docker run -p 8080:8080 healthcheck-api
```
