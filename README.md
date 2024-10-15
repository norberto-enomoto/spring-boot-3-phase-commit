# Sistema de Compra de Ingressos com 3-Phase Commit

## Visão Geral

Este projeto implementa um sistema distribuído de compra de ingressos utilizando o protocolo 3-Phase Commit (3PC) para garantir consistência em transações distribuídas. O sistema é composto por quatro microsserviços:

1. Coordinator Service
2. Ticket Service
3. Payment Service
4. Notification Service

O protocolo 3PC é utilizado para assegurar que todas as partes da transação (reserva de ingresso, pagamento e notificação) sejam concluídas com sucesso ou revertidas em caso de falha.

## Estrutura do Projeto

O projeto é dividido em quatro microsserviços independentes:

```
sistema-3pc/
│
├── coordinator-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── ticket-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── payment-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── notification-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

## Requisitos

- Java 21
- Maven
- Docker (opcional, para containerização)
- MySQL (ou outro banco de dados relacional)

## Configuração

1. Clone o repositório:
   ```
   git clone https://github.com/seu-usuario/sistema-3pc.git
   cd sistema-3pc
   ```

2. Configure o banco de dados:
   - Crie quatro bancos de dados separados para cada serviço.
   - Atualize as configurações de banco de dados em `application.properties` de cada serviço.

3. Build dos projetos:
   ```
   mvn clean package -DskipTests
   ```

## Execução

### Usando Maven

1. Inicie cada serviço separadamente:

   ```
   cd coordinator-service
   mvn spring-boot:run

   cd ../ticket-service
   mvn spring-boot:run

   cd ../payment-service
   mvn spring-boot:run

   cd ../notification-service
   mvn spring-boot:run
   ```

### Usando Docker

1. Build das imagens Docker:
   ```
   docker-compose build
   ```

2. Inicie os serviços:
   ```
   docker-compose up
   ```

## Testando o Sistema

Use o script de teste fornecido ou execute os comandos curl manualmente:

1. Crie um ticket:
   ```
   curl -X POST http://localhost:8081/api/tickets \
        -H "Content-Type: application/json" \
        -d '{"ticketId": "TICKET-123", "price": 100.00}'
   ```

2. Inicie uma transação:
   ```
   curl -X POST http://localhost:8080/api/transactions \
        -H "Content-Type: application/json" \
        -d '{"ticketId": "TICKET-123", "amount": 100.00}'
   ```

3. Verifique o status da transação:
   ```
   curl -X GET http://localhost:8080/api/transactions/{transactionId}
   ```

## Fluxo do 3-Phase Commit

1. **Fase Can-Commit**: O Coordinator verifica se todos os serviços podem realizar a transação.
2. **Fase Pre-Commit**: Se todos concordarem, o Coordinator instrui os serviços a se prepararem para a transação.
3. **Fase Do-Commit**: Se a preparação for bem-sucedida, o Coordinator instrui os serviços a finalizarem a transação.

Em caso de falha em qualquer fase, o processo de abort é iniciado, revertendo quaisquer mudanças parciais.

## Serviços

### Coordinator Service (Porta 8080)
- Gerencia o processo 3PC.
- Mantém o estado global da transação.
- Interage com os outros serviços para coordenar a transação.

### Ticket Service (Porta 8081)
- Gerencia a reserva e venda de ingressos.
- Endpoints:
  - `POST /api/tickets`: Cria um novo ticket.
  - `GET /api/tickets/{ticketId}`: Obtém informações de um ticket.
  - `POST /api/tickets/canCommit`: Verifica se um ticket pode ser reservado.
  - `POST /api/tickets/preCommit`: Reserva um ticket temporariamente.
  - `POST /api/tickets/doCommit`: Confirma a venda de um ticket.
  - `POST /api/tickets/abort`: Cancela a reserva de um ticket.

### Payment Service (Porta 8082)
- Gerencia o processamento de pagamentos.
- Endpoints:
  - `POST /api/payments/canCommit`: Verifica se o pagamento pode ser processado.
  - `POST /api/payments/preCommit`: Reserva o valor do pagamento.
  - `POST /api/payments/doCommit`: Processa o pagamento.
  - `POST /api/payments/abort`: Cancela a reserva do pagamento.

### Notification Service (Porta 8083)
- Gerencia o envio de notificações.
- Endpoints:
  - `POST /api/notifications/canCommit`: Verifica se a notificação pode ser enviada.
  - `POST /api/notifications/preCommit`: Prepara a notificação.
  - `POST /api/notifications/doCommit`: Envia a notificação.
  - `POST /api/notifications/abort`: Cancela o preparo da notificação.

## Considerações de Segurança

- Implemente autenticação e autorização entre os serviços.
- Use HTTPS para todas as comunicações em produção.
- Proteja as informações sensíveis (como credenciais de banco de dados) usando variáveis de ambiente ou serviços de configuração segura.

## Melhorias Futuras

- Implementar retry mechanisms para lidar com falhas temporárias de rede.
- Adicionar circuit breakers para melhorar a resiliência do sistema.
- Implementar logging distribuído para facilitar o rastreamento de transações.
- Adicionar métricas e monitoramento para melhor observabilidade do sistema.

## Contribuindo

Contribuições são bem-vindas! Por favor, leia o guia de contribuição antes de submeter pull requests.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).