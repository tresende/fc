# Criar as docker networks
docker network create elastic
docker network create kafka

#criar docker volumes
docker volume create es01
docker volume create kafka01
docker volume create kconnect01



docker-compose -f /Users/thiago.resende/projects/fc/codeflix/catalog-admin/sandbox/services/docker-compose.yml up mysql
#docker compose -f elk/docker-compose.yml up -d elasticsearch
docker compose -f /Users/thiago.resende/projects/fc/codeflix/catalog-videos/sandbox/kafka/docker-compose.yml up
docker compose -f /Users/thiago.resende/projects/fc/codeflix/catalog-videos/sandbox/services/docker-compose.yml up