#/bin/bash

PROFILES=$1

sudo chown root admin-catalogo/filebeat/filebeat.docker.yml
mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

# Criar as docker networks
docker network create codeflix_admin_catalogo
docker network create codeflix_catalogo
docker network create codeflix_subscription
docker network create codeflix_mysql
docker network create codeflix_elastic
docker network create codeflix_kafka
docker network create codeflix_keycloak
docker network create codeflix_rabbitmq

# Criar os docker volumes
docker volume create codeflix_filebeat01
docker volume create codeflix_es01
docker volume create codeflix_zoo01
docker volume create codeflix_kafka01
docker volume create codeflix_kconnect01
docker volume create codeflix_mysql01

# Criar as pastas com permissões
COMPOSE_PROFILES=$PROFILES docker compose -f elk/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f kafka/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f kafka-connect/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f keycloak/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f mysql/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f rabbitmq/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f admin-catalogo/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f catalogo/docker-compose.yml up -d
COMPOSE_PROFILES=$PROFILES docker compose -f subscription/docker-compose.yml up -d