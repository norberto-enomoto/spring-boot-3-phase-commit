#!/bin/bash

# Função para construir uma imagem Docker
build_image() {
    echo "Construindo $1..."
    docker build -t $1 ./$1
    if [ $? -ne 0 ]; then
        echo "Falha ao construir $1"
        exit 1
    fi
}

# Construir imagens na ordem
build_image "eureka-service"
build_image "api-gateway"
build_image "coordinator-service"
build_image "ticket-service"
build_image "payment-service"
build_image "notification-service"

echo "Todas as imagens foram construídas com sucesso!"