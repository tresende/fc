# Criar as docker networks
docker network create elastic

#criar docker volumes
docker volume create es01


docker compose -f elk/docker-compose.yml up -d elasticsearch
