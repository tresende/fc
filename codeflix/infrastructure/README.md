
# Compose Profiles

- `all`: runs all stacks.
- `admin`: runs admin application.
- `catalogo`: runs catalogo application.
- `subscription`: runs subscription application.
- `mysql`: runs only mysql.
- `elastic`: runs only elasticsearch.
- `elk`: runs ELK stack (elastic, logstash, filebeat, kibana).
- `kafka`: runs only Kafka dependencies.
- `kafka-connect`: runs only Kafka-connect.
- `keycloak`: runs only Keycloak.
- `rabbitmq`: runs only RabbitMQ.

**Running docker compose with profiles:**
```shell
COMPOSE_PROFILES=a,b,c docker compose up
```

# Executing the stack

The command to get all the stack up is:

```shell
./up.sh elastic,kafka,keycloak,rabbitmq,mysql
```

After that you can optionally run the applications with:
```shell
./up.sh admin,catalogo,subscription
```
The `admin` in the command is required because of the `depends_on` instruction in the app container.

# FAQ

1. Why Keycloak uses a bind mount instead volume?
  R: We are running Keycloak with H2 embedded database with file storage. With the Docker volume the Keycloak wasn´t able to start, keeping in a restarting loop with the exception `java.nio.file.AccessDeniedException: /opt/keycloak/data/h2/keycloakdb.trace.db`. To fix this, instead of using Docker volume we had to use Docker bind mount to a folder with permission 777.

2. Why don´t use single docker-compose file?
  R: It's just a matter of organization and personal taste. To me a single file with all this containers wasn't manageable. 

3. Why override `/etc/hosts` for the keycloak container?
  R: This configuration just make the standard access to the keycloak with the host `keycloak.internal`. 

4. Why the keycloak container overrides container hostname to `keycloak.internal`?
  R: This configuration is needed because the Keycloak can be accessed from inside and outside of the docker network. From the outside we could just point to the host `keycloak.internal` and will be fine. From the inside of the network without the container hostname override we would have to use the host `keycloak` which is the id of the container which is a different hostname from the configuration of the keycloak it self (`keycloak.internal`). This difference affects the `iss` property of the JWT known by `issuer` and breaks the authentication.