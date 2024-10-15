#!/bin/bash

# URL base do Coordinator Service
COORDINATOR_URL="http://localhost:8080/api/transactions"

# URL base do Ticket Service
TICKET_URL="http://localhost:8081/api/tickets"

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}Cenários de Teste para o Coordinator Service${NC}"
echo "============================================="

# Função para criar um ticket
create_ticket() {
    local ticket_id=$1
    local price=$2
    echo -e "\n${GREEN}Criando ticket $ticket_id${NC}"
    echo -e "${BLUE}Comando curl:${NC}"
    echo "curl -X POST $TICKET_URL \\"
    echo "     -H \"Content-Type: application/json\" \\"
    echo "     -d '{\"ticketId\": \"$ticket_id\", \"price\": $price}'"
    echo -e "${GREEN}Resposta:${NC}"
    curl -X POST $TICKET_URL \
         -H "Content-Type: application/json" \
         -d "{
             \"ticketId\": \"$ticket_id\",
             \"price\": $price
         }"
    echo
}

# Função para iniciar uma transação
start_transaction() {
    local ticket_id=$1
    local amount=$2
    echo -e "\n${GREEN}Iniciando transação para $ticket_id${NC}"
    echo -e "${BLUE}Comando curl:${NC}"
    echo "curl -X POST $COORDINATOR_URL \\"
    echo "     -H \"Content-Type: application/json\" \\"
    echo "     -d '{\"ticketId\": \"$ticket_id\", \"amount\": $amount}'"
    echo -e "${GREEN}Resposta:${NC}"
    curl -X POST $COORDINATOR_URL \
         -H "Content-Type: application/json" \
         -d "{
             \"ticketId\": \"$ticket_id\",
             \"amount\": $amount
         }"
    echo
}

# Cenário 1: Transação bem-sucedida
echo -e "\n${GREEN}Cenário 1: Transação bem-sucedida${NC}"
create_ticket "TICKET-001" 100.00
start_transaction "TICKET-001" 100.00

# Cenário 2: Transação com valor insuficiente
echo -e "\n${GREEN}Cenário 2: Transação com valor insuficiente${NC}"
create_ticket "TICKET-002" 150.00
start_transaction "TICKET-002" 100.00


# Cenário 3: Transação para ticket inexistente
echo -e "\n${GREEN}Cenário 3: Transação para ticket inexistente${NC}"
start_transaction "TICKET-999" 100.00

# Cenário 4: Transação duplicada (idempotência)
echo -e "\n${GREEN}Cenário 4: Transação duplicada (idempotência)${NC}"
create_ticket "TICKET-003" 100.00
start_transaction "TICKET-003" 100.00
start_transaction "TICKET-003" 100.00

# Cenário 5: Múltiplas transações concorrentes
echo -e "\n${GREEN}Cenário 5: Múltiplas transações concorrentes${NC}"
create_ticket "TICKET-004" 100.00
create_ticket "TICKET-005" 100.00
create_ticket "TICKET-006" 100.00

echo -e "${BLUE}Comandos curl (executados em paralelo):${NC}"
echo "curl -X POST $COORDINATOR_URL -H \"Content-Type: application/json\" -d '{\"ticketId\": \"TICKET-004\", \"amount\": 100.00}'"
echo "curl -X POST $COORDINATOR_URL -H \"Content-Type: application/json\" -d '{\"ticketId\": \"TICKET-005\", \"amount\": 100.00}'"
echo "curl -X POST $COORDINATOR_URL -H \"Content-Type: application/json\" -d '{\"ticketId\": \"TICKET-006\", \"amount\": 100.00}'"

echo -e "${GREEN}Respostas:${NC}"
start_transaction "TICKET-004" 100.00 &
start_transaction "TICKET-005" 100.00 &
start_transaction "TICKET-006" 100.00 &

wait

# Cenário 6: Transação com valor alto
echo -e "\n${GREEN}Cenário 6: Transação com valor alto${NC}"
create_ticket "TICKET-007" 5000.00
start_transaction "TICKET-007" 5000.00

echo -e "\n${GREEN}Testes concluídos${NC}"