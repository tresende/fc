# Keycloak

## Import/Export

### Exporting realm with Docker

```shell
$ docker exec -u root -t -i <container_id> /bin/bash
$ /opt/keycloak/bin/kc.sh export --realm fc3-codeflix --file /tmp/codeflix-realm.json
$ docker cp <container_id>:/tmp/codeflix-realm.json ./
```

### Importing realm with Docker

```shell
$ docker cp ./codeflix-realm.json f34715254e5e:/tmp
$ docker exec -u root -t -i f34715254e5e /bin/bash
$ /opt/keycloak/bin/kc.sh import --file /tmp/codeflix-realm.json
```