mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

docker compose -f app/docker-compose.yml up -d

echo "Inicializando os containers..."
sleep 20