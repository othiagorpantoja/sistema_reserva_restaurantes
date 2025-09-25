# 🍽️ Sistema de Reservas de Restaurante

Sistema completo de gerenciamento de reservas para restaurantes, implementado com **Clean Architecture** e **Domain-Driven Design (DDD)**.

## 🏗️ Arquitetura

O sistema segue os princípios da **Clean Architecture** com boundaries claros entre as camadas:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                      │
│  ┌─────────────────┐  ┌─────────────────┐                │
│  │   Controllers   │  │      DTOs       │                │
│  └─────────────────┘  └─────────────────┘                │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                  Application Layer                         │
│  ┌─────────────────┐  ┌─────────────────┐                │
│  │    Services     │  │    Mappers      │                │
│  └─────────────────┘  └─────────────────┘                │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                    Domain Layer                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐│
│  │    Entities     │  │  Value Objects  │  │   Events    ││
│  └─────────────────┘  └─────────────────┘  └─────────────┘│
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                 Infrastructure Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐│
│  │   Repositories  │  │   External APIs │  │   Database  ││
│  └─────────────────┘  └─────────────────┘  └─────────────┘│
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Funcionalidades

### ✨ Recursos Principais

- **Gestão de Reservas**: Criar, confirmar, cancelar e modificar reservas
- **Gestão de Mesas**: Consultar mesas disponíveis por capacidade
- **Validações Complexas**: Verificação de disponibilidade com regras de negócio
- **Domain Events**: Notificações automáticas por email e SMS
- **APIs RESTful**: Endpoints documentados com Swagger/OpenAPI
- **Integração Externa**: Serviços de email e SMS

### 🔧 Validações de Disponibilidade

- ✅ Verificação de conflitos de horário
- ✅ Validação de horário de funcionamento (11h às 23h)
- ✅ Reservas com antecedência mínima de 1 hora
- ✅ Limite de 3 meses para reservas futuras
- ✅ Detecção de horários de pico
- ✅ Verificação de feriados (extensível)

### 📧 Sistema de Notificações

- **Confirmação**: Email e SMS quando reserva é confirmada
- **Cancelamento**: Email quando reserva é cancelada
- **Modificação**: Email quando reserva é alterada
- **Conclusão**: Email de agradecimento após uso da mesa

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Swagger/OpenAPI** (documentação)
- **JUnit 5** (testes)
- **Mockito** (mocks)

## 📦 Estrutura do Projeto

```
src/
├── main/java/com/restaurant/reservation/
│   ├── domain/                          # Camada de Domínio
│   │   ├── entity/                      # Entidades de negócio
│   │   │   ├── Reservation.java
│   │   │   └── Table.java
│   │   ├── valueobject/                 # Value Objects
│   │   │   ├── ReservationId.java
│   │   │   ├── CustomerInfo.java
│   │   │   ├── ReservationTime.java
│   │   │   └── ReservationStatus.java
│   │   ├── event/                       # Domain Events
│   │   │   ├── ReservationConfirmedEvent.java
│   │   │   ├── ReservationCancelledEvent.java
│   │   │   └── ReservationCompletedEvent.java
│   │   └── repository/                  # Interfaces de repositório
│   │       ├── ReservationRepository.java
│   │       └── TableRepository.java
│   ├── application/                     # Camada de Aplicação
│   │   ├── service/                     # Serviços de aplicação
│   │   │   ├── ReservationService.java
│   │   │   ├── TableService.java
│   │   │   ├── availability/
│   │   │   │   └── AvailabilityService.java
│   │   │   ├── notification/
│   │   │   │   └── NotificationService.java
│   │   │   └── integration/
│   │   │       ├── EmailService.java
│   │   │       └── SmsService.java
│   │   ├── dto/                         # Data Transfer Objects
│   │   │   ├── CreateReservationRequest.java
│   │   │   ├── UpdateReservationRequest.java
│   │   │   ├── ReservationResponse.java
│   │   │   └── TableResponse.java
│   │   └── mapper/                      # Mappers
│   │       ├── ReservationMapper.java
│   │       └── TableMapper.java
│   ├── infrastructure/                  # Camada de Infraestrutura
│   │   ├── persistence/                 # Persistência
│   │   │   ├── entity/                  # Entidades JPA
│   │   │   │   ├── ReservationEntity.java
│   │   │   │   └── TableEntity.java
│   │   │   ├── repository/              # Repositórios JPA
│   │   │   │   ├── JpaReservationRepository.java
│   │   │   │   └── JpaTableRepository.java
│   │   │   └── mapper/                  # Mappers de persistência
│   │   │       ├── ReservationPersistenceMapper.java
│   │   │       └── TablePersistenceMapper.java
│   │   └── repository/                  # Implementações de repositório
│   │       ├── ReservationRepositoryImpl.java
│   │       └── TableRepositoryImpl.java
│   └── presentation/                    # Camada de Apresentação
│       └── controller/                  # Controllers REST
│           ├── ReservationController.java
│           └── TableController.java
└── test/                                # Testes
    └── java/com/restaurant/reservation/
        ├── domain/entity/
        │   └── ReservationTest.java
        └── application/service/
            └── ReservationServiceTest.java
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### 1. Clone o repositório

```bash
git clone <repository-url>
cd sistema_reserva_restaurantes
```

### 2. Execute a aplicação

```bash
mvn spring-boot:run
```

### 3. Acesse a aplicação

- **API**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 📚 Documentação da API

### Endpoints de Reservas

#### Criar Reserva
```http
POST /api/v1/reservations
Content-Type: application/json

{
  "tableId": "T001",
  "customerName": "João Silva",
  "customerEmail": "joao@email.com",
  "customerPhone": "(11) 99999-9999",
  "reservationDateTime": "2024-12-25T19:00:00",
  "numberOfPeople": 2,
  "durationInMinutes": 120,
  "specialRequests": "Mesa próxima à janela"
}
```

#### Buscar Reserva
```http
GET /api/v1/reservations/{id}
```

#### Confirmar Reserva
```http
PUT /api/v1/reservations/{id}/confirm
```

#### Cancelar Reserva
```http
PUT /api/v1/reservations/{id}/cancel
```

#### Atualizar Reserva
```http
PUT /api/v1/reservations/{id}
Content-Type: application/json

{
  "tableId": "T002",
  "reservationDateTime": "2024-12-25T20:00:00",
  "numberOfPeople": 4,
  "durationInMinutes": 180
}
```

### Endpoints de Mesas

#### Listar Mesas
```http
GET /api/v1/tables
```

#### Buscar Mesa por ID
```http
GET /api/v1/tables/{id}
```

#### Buscar Mesas por Capacidade
```http
GET /api/v1/tables/capacity/{capacity}
```

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Testes com relatório de cobertura
mvn test jacoco:report
```

### Cobertura de Testes

O projeto inclui testes unitários para:
- ✅ Entidades de domínio
- ✅ Serviços de aplicação
- ✅ Validações de negócio
- ✅ Mapeamento de objetos

## 🔧 Configuração

### Banco de Dados

Por padrão, o sistema usa H2 em memória. Para usar outro banco:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reservationdb
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

### APIs Externas

Configure as URLs das APIs de email e SMS:

```yaml
external:
  email:
    api-url: https://your-email-api.com
  sms:
    api-url: https://your-sms-api.com
```

## 🏗️ Padrões de Design

### Clean Architecture

- **Domain**: Regras de negócio puras, sem dependências externas
- **Application**: Casos de uso e orquestração
- **Infrastructure**: Implementações técnicas (BD, APIs, etc.)
- **Presentation**: Interface com o usuário (REST, Web, etc.)

### Domain-Driven Design (DDD)

- **Entities**: Reserva, Mesa
- **Value Objects**: ReservationId, CustomerInfo, ReservationTime
- **Domain Events**: Confirmação, Cancelamento, Modificação
- **Repositories**: Interfaces no domínio, implementações na infraestrutura

### SOLID Principles

- **S**: Cada classe tem uma responsabilidade única
- **O**: Aberto para extensão, fechado para modificação
- **L**: Substituição de Liskov respeitada
- **I**: Interfaces segregadas por funcionalidade
- **D**: Dependência de abstrações, não implementações

## 🔮 Extensões Futuras

### Funcionalidades Planejadas

- [ ] **Sistema de Pagamentos**: Integração com gateways de pagamento
- [ ] **Programa de Fidelidade**: Pontos e benefícios para clientes
- [ ] **Menu Digital**: Gestão de cardápio e pedidos
- [ ] **Analytics**: Relatórios de ocupação e performance
- [ ] **Multi-tenant**: Suporte a múltiplos restaurantes
- [ ] **Mobile App**: Aplicativo para clientes

### Melhorias Técnicas

- [ ] **Cache**: Redis para melhor performance
- [ ] **Message Queue**: RabbitMQ/Kafka para eventos assíncronos
- [ ] **Monitoring**: Prometheus + Grafana
- [ ] **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- [ ] **Security**: OAuth2 + JWT
- [ ] **Docker**: Containerização completa

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para dúvidas ou suporte, entre em contato:

- **Email**: thiago.pantoja@easynext.consulting
- **Issues**: [GitHub Issues](https://github.com/your-repo/issues)

---

**Desenvolvido com ❤️ usando Clean Architecture e Domain-Driven Design**
