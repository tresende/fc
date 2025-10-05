#/bin/bash

PROFILES=$1

# Criar as pastas com permissões
COMPOSE_PROFILES=$PROFILES docker compose -f admin-catalogo/docker-compose.yml down
COMPOSE_PROFILES=$PROFILES docker compose -f catalogo/docker-compose.yml down
COMPOSE_PROFILES=$PROFILES docker compose -f elk/docker-compose.yml down
COMPOSE_PROFILES=$PROFILES docker compose -f kafka/docker-compose.yml down
COMPOSE_PROFILES=$PROFILES docker compose -f keycloak/docker-compose.yml down
COMPOSE_PROFILES=$PROFILES docker compose -f rabbitmq/docker-compose.yml down