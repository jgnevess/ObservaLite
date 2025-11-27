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
spring.application.name=ObservaLite

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.datasource.url=jdbc:postgresql://localhost:5432/healthcheck
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

management.endpoints.web.exposure.include=health,info,metrics
```

### 3. Rodar o projeto

``` bash
./mvnw spring-boot:run
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

  | Método  | Rota                                                           |     Descrição      |
  |:-------:|:---------------------------------------------------------------|:------------------:|
  |   GET   | `/api/v1/health-checks/{projectId}/health-checks`              |       Listar       |
  |   GET   | `/api/v1/health-checks/{healthCheckId}`                        |       Buscar       |
  |   GET   | `/api/v1/health-checks/{projectId}/health-checks/date-between` |  Listar filtrado   |
  |   GET   | `/api/v1/health-checks/{projectId}/download-csv`               | Download relatório |


### Log Entry

| Método  | Rota                                           |     Descrição      |
|:-------:|:-----------------------------------------------|:------------------:|
|   GET   | `/api/v1/logs/{projectId}/logs`                |       Listar       |
|   GET   | `/api/v1/logs/{id}`                            |       Buscar       |
|   GET   | `/api/v1/logs/{projectId}/logs/date-between`   |  Listar filtrado   |
|   GET   | `/api/v1/logs/{projectId}/download-csv`        | Download relatório |

### Incidents

| Método  | Rota                                                   |     Descrição      |
|:-------:|:-------------------------------------------------------|:------------------:|
|   GET   | `/api/v1/incidents/{projectId}/incidents`              |       Listar       |
|   GET   | `/api/v1/incidents/{id}`                               |       Buscar       |
|   GET   | `/api/v1/incidents/{projectId}/incidents/date-between` |  Listar filtrado   |
|   GET   | `/api/v1/incidents/{projectId}/download-csv`           | Download relatório |

### Log Exceptions

| Método  | Rota                                  | Descrição |
|:-------:|:--------------------------------------|:---------:|
|   GET   | `/api/v1/logs/{projectId}/exceptions` |  Listar   |
|   GET   | `/api/v1/logs/exception/{id}`         |  Buscar   |

## Docker compose

### Opção 1: Apenas banco de dados (recomendado para desenvolvimento)
```bash
docker-compose -f docker-compose.db.yml up -d
```

### Opção 2: Completo (aplicação e banco)

```bash
docker compose -f docker-compose.app.yml up -d --build
```
